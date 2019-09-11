package com.tokopedia.contactus.orderquery.view.customview;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;

import com.tokopedia.contactus.common.customview.CustomTextView;
import com.tokopedia.contactus.orderquery.data.QueryTicket;

/**
 * Created by sandeepgoyal on 12/04/18.
 */

public class QueryTicketView extends CustomTextView {
    public void setQueryTicket(QueryTicket queryTicket) {
        this.queryTicket = queryTicket;
        setText(queryTicket.getName());
    }

    QueryTicket queryTicket;
    public QueryTicketView(Context context) {
        super(context);
    }

    public QueryTicketView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public QueryTicketView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void onViewClick() {

    }
}
