package com.tokopedia.purchase_platform.features.promo.presentation.listener

interface PromoCheckoutActionListener {

    fun onClickApplyRecommendedPromo()

    fun onClickApplyManualInputPromo(promoCode: String)

    fun onClickPromoListHeader(itemPosition: Int)

    fun onClickPromoListItem(itemPosition: Int)
}