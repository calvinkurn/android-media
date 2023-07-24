package com.tokopedia.promousage.domain.entity

import androidx.annotation.StringDef

sealed class PromoSection {

    companion object {

        const val SECTION_RECOMMENDATION = "recommendation_coupons"
        const val SECTION_PAYMENT = "payment_coupons"
        const val SECTION_SHIPPING = "shipping_coupons"
        const val SECTION_INPUT_PROMO_CODE = "promo_code"
    }
}

@StringDef(
    PromoSection.SECTION_RECOMMENDATION,
    PromoSection.SECTION_PAYMENT,
    PromoSection.SECTION_SHIPPING,
    PromoSection.SECTION_INPUT_PROMO_CODE
)
@Retention(AnnotationRetention.SOURCE)
annotation class PromoSectionId
