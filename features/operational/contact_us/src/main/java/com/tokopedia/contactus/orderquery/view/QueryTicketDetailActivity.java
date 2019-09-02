package com.tokopedia.contactus.orderquery.view;

import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.contactus.orderquery.data.SubmitTicketInvoiceData;
import com.tokopedia.contactus.orderquery.view.fragment.QueryTicketDetailFragment;

/**
 * Created by sandeepgoyal on 16/04/18.
 */

public class QueryTicketDetailActivity extends BaseSimpleActivity {

    public static final String KEY_QUERY_TICKET = "KEY_QUERY_TICKET";

    public static Intent getQueryTicketDetailActivity(Context context, SubmitTicketInvoiceData queryTicket) {
        Intent intent = new Intent(context, QueryTicketDetailActivity.class);
        intent.putExtra(KEY_QUERY_TICKET,queryTicket);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return QueryTicketDetailFragment.newInstance((SubmitTicketInvoiceData) getIntent().getSerializableExtra(KEY_QUERY_TICKET));
    }
}
