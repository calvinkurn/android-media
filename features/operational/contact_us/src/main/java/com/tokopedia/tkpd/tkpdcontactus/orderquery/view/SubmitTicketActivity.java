package com.tokopedia.tkpd.tkpdcontactus.orderquery.view;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.data.QueryTicket;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.data.SubmitTicketInvoiceData;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.view.fragment.SubmitTicketFragment;

/**
 * Created by sandeepgoyal on 16/04/18.
 */

public class SubmitTicketActivity extends BaseSimpleActivity{
    public static final String KEY_QUERY_TICKET = "KEY_QUERY_TICKET";

    public static Intent getSubmitTicketActivity(Context context, SubmitTicketInvoiceData queryTicket) {
        Intent intent = new Intent(context,SubmitTicketActivity.class);
        intent.putExtra(KEY_QUERY_TICKET,queryTicket);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return SubmitTicketFragment.newInstance((SubmitTicketInvoiceData) getIntent().getSerializableExtra(KEY_QUERY_TICKET));
    }

    @Override
    public void onBackPressed() {
        if(!((SubmitTicketFragment)getFragment()).onBackPressed()) {
            super.onBackPressed();
        }
        //super.onBackPressed();

    }
}
