package com.tokopedia.home.account.presentation.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.presentation.listener.AccountItemListener;
import com.tokopedia.home.account.presentation.view.buyercardview.BuyerCard;
import com.tokopedia.home.account.presentation.view.buyercardview.BuyerCardView;
import com.tokopedia.home.account.presentation.viewmodel.BuyerCardViewModel;

/**
 * @author okasurya on 7/17/18.
 */
public class BuyerCardViewHolder extends AbstractViewHolder<BuyerCardViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_buyer_card;

    private BuyerCardView buyerCardView;
    private AccountItemListener listener;

    public BuyerCardViewHolder(View itemView, AccountItemListener listener) {
        super(itemView);
        buyerCardView = itemView.findViewById(R.id.view_buyer_card);
        this.listener = listener;
    }

    @Override
    public void bind(BuyerCardViewModel element) {
        BuyerCard buyerCard = new BuyerCard.Builder()
                .avatar(element.getImageUrl())
                .username(element.getName())
                .progress(element.getProgress())
                .tokopoint(element.getTokopoint())
                .coupons(element.getCoupons())
                .build();

        buyerCardView.renderData(buyerCard);

        buyerCardView.setOnClickProfile(v -> listener.onProfileClicked(element));
        buyerCardView.setOnClickProfileCompletion(v -> listener.onProfileCompletionClicked(element));
        buyerCardView.setOnClickTokoPoint(v -> listener.onBuyerTokopointClicked(element));
        buyerCardView.setOnClickVoucher(v -> listener.onBuyerVoucherClicked(element));
        buyerCardView.setOnClickByMe(v -> listener.onByMeClicked());
    }
}
