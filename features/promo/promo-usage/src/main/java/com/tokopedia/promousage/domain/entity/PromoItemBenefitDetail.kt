package com.tokopedia.promousage.domain.entity

data class PromoItemBenefitDetail(
    val amountIdr: Double = 0.0,
    val benefitType: String = "",
    val dataType: String = ""
) {
    companion object {
        const val BENEFIT_TYPE_CASHBACK = "cashback"
        const val BENEFIT_TYPE_DISCOUNT = "discount"
        const val BENEFIT_TYPE_FREE_SHIPPING = "gratis_ongkir"
    }
}
