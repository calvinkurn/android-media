package com.tokopedia.contactus.createticket.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class ContactUsCreateTicketActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static Intent getCallingIntent(Activity context, String a, String b, String c, String d, String e, String f) {
        Toast.makeText(context, "N0-OP module found (uncomment 'contact_us_no_op' from respective .gradle files)", Toast.LENGTH_SHORT).show();
        return new Intent(context, ContactUsCreateTicketActivity.class);
    }
}
