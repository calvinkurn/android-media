package com.tokopedia.home.account.presentation.view.buyercardview;

/**
 * @author okasurya on 8/29/18.
 */
interface BuyerCardContract {
    interface View {
        void setName(String name);

        void showCompletedAvatar(String imageUrl);

        void setTokopoint(String tokopoint);

        void setTokopointTitle(String title);

        void setTokopointImageUrl(String imageUrl);

        void setCoupon(String coupons);

        void setCouponTitle(String title);

        void setCouponImageUrl(String imageUrl);

        void showBymeIcon();

        void setTokoMemberAmount(String tokoMemberAmount);

        void setTokoMemberTitle(String title);

        void setTokomemberImageUrl(String imageUrl);

        void setCardVisibility(int visibility);

        void setVisibilityCenterLayout(int visibility);

        void setVisibilityRightLayout(int visibility);

        void setVisibilityDividerFirst(int visibility);

        void setVisibilityDividerSecond(int visibility);

        void setWidgetVisibility(int visibility);

        void setEggImage(String eggImageUrl);

        void setMemberStatus(String status);

        void showSellerAccountCard(String shopName, String roleName);

        void showShopOpenCard();
    }

    interface Presenter {
        void attachView(View view);

        void detachView();

        void setData(BuyerCard buyerCard);
    }
}
