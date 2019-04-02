package com.tokopedia.saldodetails.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.saldodetails.response.model.DepositHistoryList;
import com.tokopedia.saldodetails.viewholder.SaldoListEmptyViewHolder;
import com.tokopedia.saldodetails.viewholder.SaldoTransactionViewHolder;

public class SaldoDetailTransactionFactory extends BaseAdapterTypeFactory {

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {

        AbstractViewHolder viewHolder;
        if (type == SaldoTransactionViewHolder.LAYOUT) {
            return new SaldoTransactionViewHolder(parent);
        } else if (type == SaldoListEmptyViewHolder.LAYOUT) {
            return new SaldoListEmptyViewHolder(parent);
        } else {
            viewHolder = super.createViewHolder(parent, type);
        }
        return viewHolder;
    }

    public int type(DepositHistoryList vm) {
        return SaldoTransactionViewHolder.LAYOUT;
    }

    public int type(EmptyModel vm) {
        return SaldoListEmptyViewHolder.LAYOUT;
    }
}
