package com.tokopedia.home.account.presentation.view.buyercardview;

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
        if(view != null) {
            if (buyerCard.getProgress() < 100) {
                view.showProfileProgress(buyerCard.getProgress());
                view.showIncompleteAvatar(buyerCard.getAvatar());
                view.setProfileStatusIncomplete(buyerCard.getProgress());
            } else {
                view.hideProfileProgress();
                view.showCompletedAvatar(buyerCard.getAvatar());
                view.setProfileStatusCompleted();
            }
            view.setAvatarImageUrl(buyerCard.getProgress(), buyerCard.getAvatar());
            view.setName(buyerCard.getUsername());
            view.setTokopoint(buyerCard.getTokopointAmount());
            view.setCoupon(buyerCard.getCouponAmount());
            if (buyerCard.isAffiliate()) {
                view.showBymeIcon();
            }
        }
    }
}
