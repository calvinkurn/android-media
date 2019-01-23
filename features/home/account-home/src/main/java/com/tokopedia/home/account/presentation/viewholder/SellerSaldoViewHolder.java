package com.tokopedia.home.account.presentation.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.presentation.listener.AccountItemListener;
import com.tokopedia.home.account.presentation.view.SellerSaldoView;
import com.tokopedia.home.account.presentation.viewmodel.SellerSaldoViewModel;
import com.tokopedia.showcase.ShowCaseContentPosition;
import com.tokopedia.showcase.ShowCaseObject;

import java.util.ArrayList;

public class SellerSaldoViewHolder extends AbstractViewHolder<SellerSaldoViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_seller_saldo;

    SellerSaldoView sellerSaldoView;
    private AccountItemListener listener;

    public SellerSaldoViewHolder(View view, AccountItemListener accountItemListener, ArrayList<ShowCaseObject> showCaseObjects) {
        super(view);
        sellerSaldoView = itemView.findViewById(R.id.view_seller_saldo);
        this.listener = accountItemListener;

        showCaseObjects.add(new ShowCaseObject(
                sellerSaldoView,
                getString(R.string.intro_seller_saldo_title),
                getString(R.string.intro_seller_saldo_desc),
                ShowCaseContentPosition.LEFT,
                R.color.tkpd_main_green));
    }

    @Override
    public void bind(final SellerSaldoViewModel element) {
        sellerSaldoView.setBalance(element.getDepositFmt());
        sellerSaldoView.setOnClickDeposit(v -> listener.onDepositClicked(element));
    }
}
