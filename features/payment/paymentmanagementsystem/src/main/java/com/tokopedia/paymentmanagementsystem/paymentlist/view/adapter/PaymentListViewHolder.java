package com.tokopedia.paymentmanagementsystem.paymentlist.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.paymentmanagementsystem.R;
import com.tokopedia.paymentmanagementsystem.paymentlist.view.model.PaymentListModel;

/**
 * Created by zulfikarrahman on 6/21/18.
 */

public class PaymentListViewHolder extends AbstractViewHolder<PaymentListModel> {
    public static final int LAYOUT = R.layout.item_payment_list;

    public PaymentListViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(PaymentListModel element) {

    }
}
