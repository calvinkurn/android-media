package com.tokopedia.home.account.presentation.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.presentation.listener.AccountItemListener;
import com.tokopedia.home.account.presentation.view.ShopCardView;
import com.tokopedia.home.account.presentation.viewmodel.ShopCardViewModel;

/**
 * @author okasurya on 7/26/18.
 */
public class ShopCardViewHolder extends AbstractViewHolder<ShopCardViewModel> {
    @LayoutRes
    public static final int LAYOUT = R.layout.item_shop_card;

    private ShopCardView shopCardView;
    private AccountItemListener listener;

    public ShopCardViewHolder(View view, AccountItemListener listener) {
        super(view);
        shopCardView = itemView.findViewById(R.id.view_shop_card);
        this.listener = listener;
    }

    @Override
    public void bind(ShopCardViewModel element) {
        shopCardView.setShopName(element.getShopName());
        shopCardView.setShopImage(element.getShopImageUrl());
        if(element.getGoldMerchant()) {
            shopCardView.setBadgeImage(R.drawable.ic_gm_badge);
        }
        shopCardView.setShopReputation(element.getMedalType(), element.getLevel());
        shopCardView.setBalance(element.getBalance());

        shopCardView.setOnClickShopAvatar(v -> listener.onShopAvatarClicked(element));
        shopCardView.setOnClickShopName(v -> listener.onShopNameClicked(element));
    }
}
