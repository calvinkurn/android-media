package com.tokopedia.home.account.presentation.view.buyercardview;

/**
 * @author okasurya on 8/29/18.
 */
interface BuyerCardContract {
    interface View {
        void setName(String name);

        void setAvatarImageUrl(int progress, String imageUrl);

        void showCompletedAvatar(String imageUrl);

        void showIncompleteAvatar(String imageUrl);

        void setTokopoint(String tokopoint);

        void setCoupon(int coupons);

        void showProfileProgress(int progress);

        void hideProfileProgress();

        void setProfileStatusCompleted();

        void setProfileStatusIncomplete(int progress);

        void showBymeIcon();
    }

    interface Presenter {
        void attachView(View view);

        void detachView();

        void setData(BuyerCard buyerCard);
    }
}
