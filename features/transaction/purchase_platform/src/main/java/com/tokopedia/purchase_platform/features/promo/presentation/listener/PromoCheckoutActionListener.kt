package com.tokopedia.purchase_platform.features.promo.presentation.listener

interface PromoCheckoutActionListener {

    fun onClickApplyManualInputPromo(promoCode: String)

    fun onClickApplyRecommendedPromo()

    fun onClickPromoListHeader(itemPosition: Int)
}