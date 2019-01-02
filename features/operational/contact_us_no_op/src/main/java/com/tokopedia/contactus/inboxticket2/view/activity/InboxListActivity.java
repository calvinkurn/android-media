package com.tokopedia.contactus.inboxticket2.view.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


public class InboxListActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, InboxListActivity.class);
    }
}
