package com.example.doeljack.kelompokku;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.annotation.Target;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth.AuthStateListener mAuthListener;

    private EditText mNameField;
    private EditText mEmailField;
    private EditText mPasswordField;

    private Button mRegisterbtn;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ProgressDialog mProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Users");
        mProgress = new ProgressDialog(this);

        mNameField = (EditText)findViewById(R.id.editText);
        mEmailField = (EditText) findViewById(R.id.editText2);
        mPasswordField = (EditText) findViewById(R.id.editText3);
        mRegisterbtn = (Button) findViewById(R.id.button);

        // Connect to the Firebase database
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        // Get a reference to the todoItems child items it the database
        final DatabaseReference myRef = database.getReference("todoItems");

        // Assign a listener to detect changes to the child items
        // of the database reference.


        // Add items via the Button and EditText at the bottom of the window.
        final EditText nama = (EditText) findViewById(R.id.editText);
        final EditText email = (EditText) findViewById(R.id.editText2);
        final EditText password = (EditText) findViewById(R.id.editText3);
        final Button button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Create a new child with a auto-generated ID.
                DatabaseReference childRef = myRef.push();
                Intent i=new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);//target = nama class
                // Set the child's data to the value passed in from the text box.
                childRef.setValue(nama.getText().toString());
                childRef.setValue(email.getText().toString());
                childRef.setValue(password.getText().toString());
            }
        });

        mRegisterbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                startRegister();
            }
        });
    }

    private void startRegister(){

        final String name = mNameField.getText().toString().trim();
        String email = mEmailField.getText().toString().trim();
        String password = mPasswordField.getText().toString().trim();

        if (!TextUtils.isEmpty(name)&& ! TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){

            mProgress.setMessage("Singing Up .........");
            mProgress.show();

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        String user_id = mAuth.getCurrentUser().getUid();
                        DatabaseReference current_user_db = mDatabase.child(user_id);
                        current_user_db.child("name").setValue(name);
                        current_user_db.child("image").setValue("default");
                        mProgress.dismiss();

                        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainIntent);


                    }
                }
            });
        }
    }
}
