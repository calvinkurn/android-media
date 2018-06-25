package com.tokopedia.paymentmanagementsystem.paymentlist.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.paymentmanagementsystem.paymentlist.view.model.PaymentListModel;

/**
 * Created by zulfikarrahman on 6/21/18.
 */

public class PaymentListAdapterTypeFactory extends BaseAdapterTypeFactory{

    public int type(PaymentListModel paymentListModel) {
        return PaymentListViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if(type == PaymentListViewHolder.LAYOUT){
            return new PaymentListViewHolder(parent);
        }
        return super.createViewHolder(parent, type);
    }
}
