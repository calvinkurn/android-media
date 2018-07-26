package com.tokopedia.home.account.presentation.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.presentation.adapter.AccountTypeFactory;
import com.tokopedia.home.account.presentation.view.BuyerCardView;
import com.tokopedia.home.account.presentation.viewmodel.BuyerCardViewModel;

/**
 * @author okasurya on 7/17/18.
 */
public class BuyerCardViewHolder extends AbstractViewHolder<BuyerCardViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_buyer_card;

    private BuyerCardView buyerCardView;
    private AccountTypeFactory.Listener listener;

    public BuyerCardViewHolder(View itemView, AccountTypeFactory.Listener listener) {
        super(itemView);
        buyerCardView = itemView.findViewById(R.id.view_buyer_card);
        this.listener = listener;
    }

    @Override
    public void bind(BuyerCardViewModel element) {
        buyerCardView.setAvatarImageUrl(element.getImageUrl());
        buyerCardView.setName(element.getName());
        buyerCardView.setProfileCompletion(element.getProgress());
        buyerCardView.setTokopoint(element.getTokopoint());
        buyerCardView.setVoucher(element.getVoucher());
    }
}
