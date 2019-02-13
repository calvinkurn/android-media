package com.tokopedia.contactus.createticket.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class ContactUsActivity extends Activity {
    public static String PARAM_TOOLBAR_TITLE = "test";
    public static final String EXTRAS_PARAM_TOOLBAR_TITLE = "EXTRAS_PARAM_TOOLBAR_TITLE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(getApplicationContext(), "N0-OP module found (uncomment 'contact_us_no_op' from respective .gradle files)", Toast.LENGTH_SHORT).show();
        finish();
    }
}
