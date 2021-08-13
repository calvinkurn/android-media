package com.tokopedia.home.account.presentation.view.buyercardview;

import android.opengl.Visibility;

import com.tokopedia.home.account.presentation.view.buyercardview.BuyerCardContract.View;

import static android.view.View.GONE;

/**
 * @author okasurya on 8/29/18.
 */
public class BuyerCardPresenter implements BuyerCardContract.Presenter {
    private BuyerCardContract.View view;

    @Override
    public void attachView(BuyerCardContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void setData(BuyerCard buyerCard) {
        if (view != null) {
            view.showCompletedAvatar(buyerCard.getAvatar());
            view.setName(buyerCard.getUsername());
            view.setTokopointTitle(buyerCard.getTokopointTitle());
            view.setTokopoint(buyerCard.getTokopointAmount());
            view.setTokopointImageUrl(buyerCard.getTokopointImageUrl());
            view.setCouponTitle(buyerCard.getCouponTitle());
            view.setCoupon(buyerCard.getCouponAmount());
            view.setCouponImageUrl(buyerCard.getCouponImageUrl());
            view.setTokoMemberTitle(buyerCard.getTokomemberTitle());
            view.setTokoMemberAmount(buyerCard.getTokoMemberAmount());
            view.setTokomemberImageUrl(buyerCard.getTokomemberImageUrl());
            view.setEggImage(buyerCard.getEggImageUrl());
            view.setMemberStatus(buyerCard.getMemberStatus());

            if(buyerCard.isHasShop() || (buyerCard.getRoleName() != null && !buyerCard.getRoleName().isEmpty())) {
                view.showSellerAccountCard(buyerCard.getShopName(), buyerCard.getRoleName());
            } else {
                view.showShopOpenCard();
            }

            if (buyerCard.getTokopointSize() == 0) {
                view.setCardVisibility(GONE);
            }
            if (buyerCard.getCouponSize() == 0) {
                view.setVisibilityCenterLayout(GONE);
                view.setVisibilityDividerFirst(GONE);
            }
            if (buyerCard.getTokomemberSize() == 0) {
                view.setVisibilityRightLayout(GONE);
                view.setVisibilityDividerSecond(GONE);
            }

            if (buyerCard.getTokopointSize() == 0 && buyerCard.getCouponSize() == 0 && buyerCard.getTokomemberSize() == 0){
                view.setWidgetVisibility(GONE);
            }
        }
    }
}
