package com.tokopedia.promousage.domain.entity

sealed interface PromoPageSection {

    companion object {
        const val SECTION_RECOMMENDATION = "recommendation_coupons"
        const val SECTION_PAYMENT = "payment_coupons"
        const val SECTION_SHIPPING = "shipping_coupons"
        const val SECTION_INPUT_PROMO_CODE = "promo_code"
    }
}
