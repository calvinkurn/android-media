package com.tokopedia.saldodetails.adapter;

import android.support.annotation.NonNull;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.saldodetails.presentation.listener.SaldoItemListener;
import com.tokopedia.saldodetails.response.model.Deposit;
import com.tokopedia.saldodetails.viewholder.SaldoTransactionViewHolder;

public class SaldoDetailTransactionFactory extends BaseAdapterTypeFactory {

    private SaldoItemListener listener;

    public SaldoDetailTransactionFactory(@NonNull SaldoItemListener listener) {
        this.listener = listener;
    }


    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == SaldoTransactionViewHolder.LAYOUT) {
            return new SaldoTransactionViewHolder(parent, listener);
        }
        return super.createViewHolder(parent, type);
    }

    public int type(Deposit vm) {
        return SaldoTransactionViewHolder.LAYOUT;
    }
}
