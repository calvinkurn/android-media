package com.tokopedia.tokopoints.view.cataloglisting;

import com.tokopedia.tokopoints.view.model.CatalogsValueEntity;

public interface CatalogPurchaseRedemptionPresenter {

    void redeemCoupon(String promoCode, String cta);

    void startSaveCoupon(CatalogsValueEntity item);

}
