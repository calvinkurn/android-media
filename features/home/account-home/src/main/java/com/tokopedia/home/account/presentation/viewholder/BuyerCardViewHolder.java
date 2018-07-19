package com.tokopedia.home.account.presentation.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.presentation.view.BuyerCardView;
import com.tokopedia.home.account.presentation.viewmodel.BuyerCardViewModel;

/**
 * @author okasurya on 7/17/18.
 */
public class BuyerCardViewHolder extends AbstractViewHolder<BuyerCardViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_buyer_card;

    private BuyerCardView buyerCardView;

    public BuyerCardViewHolder(View itemView) {
        super(itemView);
        buyerCardView = itemView.findViewById(R.id.view_buyer_card);
    }

    @Override
    public void bind(BuyerCardViewModel element) {
        buyerCardView.setAvatarImageUrl(element.getImageUrl());
        buyerCardView.setName(element.getName());
        buyerCardView.setProgress(element.getProgress());
        buyerCardView.setTokopoint(element.getTokopoint());
        buyerCardView.setVoucher(element.getVoucher());
    }
}
