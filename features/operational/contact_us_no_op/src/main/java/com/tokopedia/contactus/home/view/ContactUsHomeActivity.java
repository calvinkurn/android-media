package com.tokopedia.contactus.home.view;


import android.app.Activity;

import android.os.Bundle;
import android.widget.Toast;

public class ContactUsHomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(getApplicationContext(), "N0-OP module found (uncomment 'contact_us_no_op' from respective .gradle files)", Toast.LENGTH_SHORT).show();
        finish();
    }
}
