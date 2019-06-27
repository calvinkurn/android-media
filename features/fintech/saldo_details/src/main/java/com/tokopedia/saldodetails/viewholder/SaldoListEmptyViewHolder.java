package com.tokopedia.saldodetails.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.saldodetails.R;

public class SaldoListEmptyViewHolder extends AbstractViewHolder<EmptyModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.saldo_transaction_list_empty;

    public SaldoListEmptyViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(EmptyModel element) {

    }
}
