package com.tokopedia.home.account.presentation.viewholder;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.presentation.adapter.AccountTypeFactory;
import com.tokopedia.home.account.presentation.listener.AccountItemListener;
import com.tokopedia.home.account.presentation.view.TokopediaPayCardView;
import com.tokopedia.home.account.presentation.viewmodel.TokopediaPayViewModel;

/**
 * @author okasurya on 7/19/18.
 */
public class TokopediaPayViewHolder extends AbstractViewHolder<TokopediaPayViewModel> {
    @LayoutRes
    public static int LAYOUT = R.layout.item_tokopedia_pay;

    private TokopediaPayCardView tokopediaPayCardView;
    private AccountItemListener listener;

    public TokopediaPayViewHolder(View itemView, @NonNull AccountItemListener listener) {
        super(itemView);
        tokopediaPayCardView = itemView.findViewById(R.id.view_tokopedia_pay);
        this.listener = listener;
    }

    @Override
    public void bind(TokopediaPayViewModel element) {
        tokopediaPayCardView.setTextAmountLeft(element.getAmountLeft());
        tokopediaPayCardView.setTextDescLeft(element.getLabelLeft());
        tokopediaPayCardView.setTextAmountRight(element.getAmountRight());
        tokopediaPayCardView.setTextDesctRight(element.getLabelRight());
        tokopediaPayCardView.setActionTextClickListener(v -> listener.onTokopediaPayLinkClicked());
    }
}
