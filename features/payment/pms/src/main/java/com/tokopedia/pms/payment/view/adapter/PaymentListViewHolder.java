package com.tokopedia.pms.payment.view.adapter;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.pms.R;
import com.tokopedia.pms.payment.view.model.PaymentListModel;

/**
 * Created by zulfikarrahman on 6/21/18.
 */

public class PaymentListViewHolder extends AbstractViewHolder<PaymentListModel> {
    public static final int LAYOUT = R.layout.item_payment_list;

    private ListenerPaymentList listenerPaymentList;

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
    private TextView howToPay;
    private TextView cancelTransaction;

    public PaymentListViewHolder(View itemView, ListenerPaymentList listenerPaymentList) {
        super(itemView);
        this.listenerPaymentList = listenerPaymentList;
        imageProduct = itemView.findViewById(R.id.image_product_type);
        transactionDate = itemView.findViewById(R.id.tv_order_date);
        productName = itemView.findViewById(R.id.tv_product_type);
        imageOverflow = itemView.findViewById(R.id.iv_overflow);
        containerTicker = itemView.findViewById(R.id.container_due_date_payment);
        tickerMessage = itemView.findViewById(R.id.tv_due_payment_date);
        imageBank = itemView.findViewById(R.id.image_payment);
        totalTransaction = itemView.findViewById(R.id.tv_value_total);
        containerDetailBankTransfer = itemView.findViewById(R.id.container_detail_payment_bank_transfer);
        containerDetailPaymentDefault = itemView.findViewById(R.id.container_detail_payment);
        seeDetail = itemView.findViewById(R.id.tv_see_detail);
        accountUserNo = itemView.findViewById(R.id.tv_number_sender_account);
        accountUserName = itemView.findViewById(R.id.tv_name_sender_account);
        accountDestNo = itemView.findViewById(R.id.tv_number_receiver_account);
        accountDestName = itemView.findViewById(R.id.tv_name_receiver_account);
        labelDynamicView = itemView.findViewById(R.id.tv_payment_code);
        valueDynamicView = itemView.findViewById(R.id.tv_value_payment_code);
        paymentMethod = itemView.findViewById(R.id.tv_value_payment_method);
        howToPay = itemView.findViewById(R.id.button_how_to_pay);
        cancelTransaction = itemView.findViewById(R.id.button_cancel_payment);
    }

    @Override
    public void bind(PaymentListModel element) {
        ImageHandler.LoadImage(imageProduct, element.getProductImage());
        transactionDate.setText(element.getTransaction_date());
        productName.setText(element.getProductName());
        tickerMessage.setText(element.getTickerMessage());
        ImageHandler.LoadImage(imageBank, element.getPaymentImage());
        totalTransaction.setText(CurrencyFormatUtil.convertPriceValueToIdrFormat(element.getPaymentAmount(), true));
        accountUserNo.setText(element.getUserAccountNo());
        accountUserName.setText(getString(R.string.payment_label_name_acc_formated,element.getUserAccountName()));
        accountDestNo.setText(element.getDestAccountNo());
        accountDestName.setText(getString(R.string.payment_label_name_acc_formated,element.getDestAccountName()));
        labelDynamicView.setText(element.getLabelDynamicViewDetailPayment());
        valueDynamicView.setText(element.getValueDynamicViewDetailPayment());
        paymentMethod.setText(element.getPaymentMethod());

        imageOverflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listenerPaymentList.onClickOverFlow(element);
            }
        });
        howToPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listenerPaymentList.onClickHowToPay(element);
            }
        });
        cancelTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listenerPaymentList.onClickCancelTransaction(element);
            }
        });
        seeDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listenerPaymentList.onClickSeeDetail(element.getInvoiceUrl());
            }
        });

        if(element.getListOfAction().size() > 0){
            imageOverflow.setVisibility(View.VISIBLE);
        }else{
            imageOverflow.setVisibility(View.GONE);
        }

        if(element.isShowSeeDetail()){
            seeDetail.setVisibility(View.VISIBLE);
        }else{
            seeDetail.setVisibility(View.GONE);
        }

        if(element.isShowCancelTransaction()){
            cancelTransaction.setVisibility(View.GONE);
            howToPay.setVisibility(View.GONE);
        }else if(element.isShowHowToPay()){
            cancelTransaction.setVisibility(View.GONE);
            howToPay.setVisibility(View.VISIBLE);
        }else{
            cancelTransaction.setVisibility(View.GONE);
            howToPay.setVisibility(View.GONE);
        }

        if(element.isShowTickerMessage()){
            containerTicker.setVisibility(View.VISIBLE);
        }else{
            containerTicker.setVisibility(View.GONE);
        }

        if(element.isShowContainerDetailBankTransfer()){
            containerDetailBankTransfer.setVisibility(View.VISIBLE);
            containerDetailPaymentDefault.setVisibility(View.GONE);
        }else{
            containerDetailPaymentDefault.setVisibility(View.VISIBLE);
            containerDetailBankTransfer.setVisibility(View.GONE);
        }

        if(element.isShowDynamicLabelDetailPayment()){
            labelDynamicView.setVisibility(View.VISIBLE);
            valueDynamicView.setVisibility(View.VISIBLE);
        }else{
            labelDynamicView.setVisibility(View.GONE);
            valueDynamicView.setVisibility(View.GONE);
        }
    }

    public interface ListenerPaymentList{
        void onClickOverFlow(PaymentListModel paymentListModel);

        void onClickHowToPay(PaymentListModel element);

        void onClickCancelTransaction(PaymentListModel element);

        void onClickSeeDetail(String invoiceUrl);
    }
}
