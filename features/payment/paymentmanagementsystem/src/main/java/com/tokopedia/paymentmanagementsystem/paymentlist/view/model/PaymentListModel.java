package com.tokopedia.paymentmanagementsystem.paymentlist.view.model;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.paymentmanagementsystem.paymentlist.view.adapter.PaymentListAdapterTypeFactory;

/**
 * Created by zulfikarrahman on 6/21/18.
 */

public class PaymentListModel implements Visitable<PaymentListAdapterTypeFactory> {
    @Override
    public int type(PaymentListAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
