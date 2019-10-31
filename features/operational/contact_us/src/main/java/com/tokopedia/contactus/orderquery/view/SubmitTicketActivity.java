package com.tokopedia.contactus.orderquery.view;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PersistableBundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.contactus.ContactUsModuleRouter;
import com.tokopedia.contactus.createticket.ContactUsConstant;
import com.tokopedia.contactus.orderquery.data.SubmitTicketInvoiceData;
import com.tokopedia.contactus.orderquery.view.broadcastreceiver.FinishActivityReceiver;
import com.tokopedia.contactus.orderquery.view.fragment.SubmitTicketFragment;

/**
 * Created by sandeepgoyal on 16/04/18.
 */

public class SubmitTicketActivity extends BaseSimpleActivity {
    public static final String KEY_QUERY_TICKET = "KEY_QUERY_TICKET";
    private FinishActivityReceiver finishReceiver = new FinishActivityReceiver(this);

    public static Intent getSubmitTicketActivity(Context context, SubmitTicketInvoiceData queryTicket) {
        Intent intent = new Intent(context, SubmitTicketActivity.class);
        intent.putExtra(KEY_QUERY_TICKET, queryTicket);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return SubmitTicketFragment.newInstance((SubmitTicketInvoiceData) getIntent().getSerializableExtra(KEY_QUERY_TICKET));
    }

    @Override
    public void onBackPressed() {
        if (!((SubmitTicketFragment) getFragment()).onBackPressed()) {
            super.onBackPressed();
        }
        //super.onBackPressed();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ContactUsConstant.ACTION_CLOSE_ACTIVITY);
        LocalBroadcastManager.getInstance(this).registerReceiver(finishReceiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isFinishing())
            LocalBroadcastManager.getInstance(this).unregisterReceiver(finishReceiver);
    }
}
