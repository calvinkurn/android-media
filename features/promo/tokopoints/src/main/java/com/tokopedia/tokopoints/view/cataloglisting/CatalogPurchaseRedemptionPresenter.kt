package com.tokopedia.tokopoints.view.cataloglisting

import com.tokopedia.tokopoints.view.model.CatalogsValueEntity

interface CatalogPurchaseRedemptionPresenter {
    fun redeemCoupon(promoCode: String?, cta: String?)
    fun startSaveCoupon(item: CatalogsValueEntity)
}
