package com.tokopedia.pms.payment.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.pms.payment.view.model.PaymentListModel;

/**
 * Created by zulfikarrahman on 6/21/18.
 */

public class PaymentListAdapterTypeFactory extends BaseAdapterTypeFactory{

    private PaymentListViewHolder.ListenerPaymentList listenerPaymentList;

    public PaymentListAdapterTypeFactory(PaymentListViewHolder.ListenerPaymentList listenerPaymentList) {
        this.listenerPaymentList = listenerPaymentList;
    }

    public int type(PaymentListModel paymentListModel) {
        return PaymentListViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if(type == PaymentListViewHolder.LAYOUT){
            return new PaymentListViewHolder(parent, listenerPaymentList);
        }
        return super.createViewHolder(parent, type);
    }
}
