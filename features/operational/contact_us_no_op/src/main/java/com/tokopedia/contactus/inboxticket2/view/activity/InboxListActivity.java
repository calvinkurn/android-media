package com.tokopedia.contactus.inboxticket2.view.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;


public class InboxListActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static Intent getCallingIntent(Context context) {
        Toast.makeText(context, "N0-OP module found (uncomment 'contact_us_no_op' from respective .gradle files)", Toast.LENGTH_SHORT).show();
        return new Intent(context, InboxListActivity.class);
    }
}
