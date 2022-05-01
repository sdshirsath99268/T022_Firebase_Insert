package com.example.t022_firebase_insert;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    EditText name , email , password , contact ;
    Button BtnRegister ;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        contact = findViewById(R.id.contact);
        BtnRegister = findViewById(R.id.BtnRegister);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("user");

        BtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String NAME = name.getText().toString();
                String EMAIL = email.getText().toString();
                String PASSWORD = password.getText().toString();
                String CONTACT = contact.getText().toString();

                if (NAME.isEmpty()){
                    name.setError("Name is Required");
                    name.requestFocus();
                    return;
                }

                if (EMAIL.isEmpty()){
                    email.setError("Email is Required");
                    email.requestFocus();
                    return;
                }

                if (PASSWORD.isEmpty()){
                    password.setError("Password is Required");
                    password.requestFocus();
                    return;
                }

                if (CONTACT.isEmpty()){
                    contact.setError("Contact No is Required");
                    contact.requestFocus();
                    return;
                }

                firebaseAuth.createUserWithEmailAndPassword(EMAIL,PASSWORD).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                           // Toast.makeText(getApplicationContext(), "User Added", Toast.LENGTH_SHORT).show();
                            // Insert Data

                            String ID = databaseReference.push().getKey();
                            Model model = new Model(ID,NAME,EMAIL,PASSWORD,CONTACT);
                            databaseReference.child(ID).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "User Data Inserted", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                                }
                            });

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage() , Toast.LENGTH_SHORT).show();
                    }
                });



            }
        });
    }
}