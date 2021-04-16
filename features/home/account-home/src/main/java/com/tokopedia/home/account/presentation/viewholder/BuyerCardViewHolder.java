package com.tokopedia.home.account.presentation.viewholder;

import android.view.View;

import androidx.annotation.LayoutRes;

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
                .shopName(element.getShopName())
                .progress(element.getProgress())
                .tokopointTitle(element.getTokopointTitle())
                .tokopoint(element.getTokopoint())
                .tokopointImageUrl(element.getTokopointImageUrl())
                .tokopointApplink(element.getTokopointAppplink())
                .couponsTitle(element.getCouponTitle())
                .coupons(element.getCoupons())
                .couponImageUrl(element.getCouponImageUrl())
                .couponApplink(element.getCouponApplink())
                .isAffliate(element.isAffiliate())
                .isHasShop(element.isHasShop())
                .roleName(element.getRoleName())
                .tokomemberTitle(element.getTokomemberTitle())
                .tokomember(element.getTokomember())
                .tokomemberImageUrl(element.getTokomemberImageUrl())
                .tokomemberApplink(element.getTokomemberApplink())
                .tokopointSize(element.getTokopointSize())
                .tokomemberSize(element.getTokomemberSize())
                .couponSize(element.getCouponSize())
                .memberStatus(element.getMemberStatus())
                .eggImageUrl(element.getEggImageUrl())
                .build();

        buyerCardView.renderData(buyerCard);

        buyerCardView.setOnClickProfileCompletion(v -> listener.onProfileClicked(element));
        buyerCardView.setOnClickMemberDetail(v -> listener.onClickMemberDetail());
        buyerCardView.setOnClickTokoPoint(v -> listener.onBuyerTokopointClicked(element.getTokopointAppplink(), element.getTokopointTitle()));
        buyerCardView.setOnClickTokoMember(v -> listener.onTokomemberClicked(element.getTokomemberApplink(), element.getTokomemberTitle()));
        buyerCardView.setOnClickVoucher(v -> listener.onBuyerVoucherClicked(element.getCouponApplink(), element.getCouponTitle()));
        buyerCardView.setOnClickByMe(v -> listener.onByMeClicked());
        buyerCardView.setOnClickSellerAccount(v -> listener.onSellerAccountCardClicked());
        buyerCardView.setOnclickIconWarningName(v -> listener.onIconWarningNameClick(element));
    }
}
