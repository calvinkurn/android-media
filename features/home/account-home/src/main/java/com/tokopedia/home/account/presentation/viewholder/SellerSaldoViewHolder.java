package com.tokopedia.home.account.presentation.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.presentation.listener.AccountItemListener;
import com.tokopedia.home.account.presentation.view.SellerSaldoView;
import com.tokopedia.home.account.presentation.viewmodel.SellerSaldoViewModel;

public class SellerSaldoViewHolder extends AbstractViewHolder<SellerSaldoViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_seller_saldo;
    private SellerSaldoView sellerSaldoView;
    private AccountItemListener listener;

    public SellerSaldoViewHolder(View view, AccountItemListener accountItemListener) {
        super(view);
        sellerSaldoView = itemView.findViewById(R.id.view_seller_saldo);
        this.listener = accountItemListener;
    }

    @Override
    public void bind(final SellerSaldoViewModel element) {
        sellerSaldoView.setBalance(element.getDepositFmt());
        sellerSaldoView.setOnClickDeposit(v -> listener.onDepositClicked(element));
    }
}
