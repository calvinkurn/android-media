package com.tokopedia.tokopoints.view.contract;

import com.tokopedia.tokopoints.view.model.CatalogsValueEntity;

public interface CatalogPurchaseRedemptionPresenter {
    void startValidateCoupon(CatalogsValueEntity item);

    void redeemCoupon(String promoCode, String cta);

    void startSaveCoupon(CatalogsValueEntity item);

    void navigateToWebView(String url);

    void showRedeemCouponDialog(String cta, String code, String title);

    void startSendGift(int id, int isGift);
}
