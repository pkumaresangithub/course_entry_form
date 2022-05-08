package com.example.courseentry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Entryform extends AppCompatActivity {

    EditText category,author,subject,tutor,institute;
    TextView form_header;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entryform);
        Intent intent = getIntent();
        String CATNO = intent.getStringExtra("catnokey");

        Toast.makeText(getApplicationContext(),CATNO,Toast.LENGTH_SHORT).show();

        category = findViewById(R.id.edt_category);
        author = findViewById(R.id.edt_author);
        subject = findViewById(R.id.edt_subject);
        tutor = findViewById(R.id.edt_tutorname);
        institute = findViewById(R.id.edt_institution);
        form_header = findViewById(R.id.form_header);

        form_header.setText(CATNO+" Entry forms");

        save = findViewById(R.id.btnsave);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String CATNAME = category.getText().toString();
                String AUTHOR = author.getText().toString();
                String SUBJECT = subject.getText().toString();
                String TUTOR = tutor.getText().toString();
                String INSTITUE= institute.getText().toString();

                firebaseAuth = FirebaseAuth.getInstance();
                databaseReference = FirebaseDatabase.getInstance().getReference("course");

                if (CATNAME.isEmpty()){
                    category.setError("Category is required");
                    category.requestFocus();
                    return;
                }

                if (AUTHOR.isEmpty()){
                    author.setError("Author id is required");
                    author.requestFocus();
                    return;
                }
                if (SUBJECT.isEmpty()){
                    subject.setError("Subject is required");
                    subject.requestFocus();
                    return;
                }
                if (TUTOR.isEmpty()){
                    tutor.setError("Tutor Name is required");
                    tutor.requestFocus();
                    return;
                }
                if (INSTITUE.isEmpty()){
                    institute.setError("Date of birth is required");
                    institute.requestFocus();
                    return;
                }
                String EMAILID ="pkumaresanin@gmail.com";
                String PASSWORD ="bd290371";
                firebaseAuth.signInWithEmailAndPassword(EMAILID,PASSWORD).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
//                            Toast.makeText(getApplicationContext(), "User Added ", Toast.LENGTH_SHORT).show();

                            //                         Insert data
                            String ID = databaseReference.push().getKey();
                            Model model = new Model(ID,CATNO,CATNAME,SUBJECT,AUTHOR,TUTOR,INSTITUE);
                            databaseReference.child(ID).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Course Detail Added", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            });


                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


        };



        });

    }




}