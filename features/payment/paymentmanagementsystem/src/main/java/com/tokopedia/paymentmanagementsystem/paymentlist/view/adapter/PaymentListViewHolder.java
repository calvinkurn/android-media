package com.tokopedia.paymentmanagementsystem.paymentlist.view.adapter;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.paymentmanagementsystem.R;
import com.tokopedia.paymentmanagementsystem.paymentlist.view.model.PaymentListModel;

/**
 * Created by zulfikarrahman on 6/21/18.
 */

public class PaymentListViewHolder extends AbstractViewHolder<PaymentListModel> {
    public static final int LAYOUT = R.layout.item_payment_list;

    private ImageView imageProduct;
    private TextView transactionDate;
    private TextView productName;
    private ImageView imageOverflow;
    private View containerTicker;
    private TextView tickerMessage;
    private ImageView imageBank;
    private TextView totalTransaction;
    private View containerDetailBankTransfer;
    private View containerDetailPaymentDefault;
    private TextView seeDetail;
    private TextView accountUserNo;
    private TextView accountUserName;
    private TextView accountDestNo;
    private TextView accountDestName;
    private TextView labelDynamicView;
    private TextView valueDynamicView;
    private TextView paymentMethod;
    private Button howToPay;
    private Button cancelTransaction;

    public PaymentListViewHolder(View itemView) {
        super(itemView);
        imageProduct = itemView.findViewById(R.id);
        transactionDate = itemView.findViewById(R.id);
        productName = itemView.findViewById(R.id);
    }

    @Override
    public void bind(PaymentListModel element) {

    }
}
