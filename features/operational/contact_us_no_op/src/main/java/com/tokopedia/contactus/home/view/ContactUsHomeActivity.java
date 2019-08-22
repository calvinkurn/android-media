package com.tokopedia.contactus.home.view;


import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class ContactUsHomeActivity extends Activity {

    public static Intent getContactUsHomeIntent(Context context, Bundle extra) {
        return new Intent();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(getApplicationContext(), "N0-OP module found (uncomment 'contact_us_no_op' from respective .gradle files)", Toast.LENGTH_SHORT).show();
        finish();
    }
}
