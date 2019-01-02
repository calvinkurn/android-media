package com.tokopedia.contactus.createticket.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class ContactUsCreateTicketActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static Intent getCallingIntent(Activity context, String a, String b, String c, String d, String e, String f) {
        return new Intent(context, ContactUsCreateTicketActivity.class);
    }
}
