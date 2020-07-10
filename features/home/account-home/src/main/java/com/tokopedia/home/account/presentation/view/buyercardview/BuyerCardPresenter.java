package com.tokopedia.home.account.presentation.view.buyercardview;

import android.opengl.Visibility;

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

            if (buyerCard.getTokopointSize() == 0) {
                view.setCardVisibility(0);
            }
            if (buyerCard.getCouponSize() == 0) {
                view.setVisibilityCenterLayout(0);
                view.setVisibilityDividerFirst(0);
            }
            if (buyerCard.getTokomemberSize() == 0) {
                view.setVisibilityRightLayout(0);
                view.setVisibilityDividerSecond(0);
            }

            if (buyerCard.getTokopointSize() == 0 && buyerCard.getCouponSize() == 0 && buyerCard.getTokomemberSize() == 0){
                view.setWidgetVisibility(0);
            }
        }
    }
}
