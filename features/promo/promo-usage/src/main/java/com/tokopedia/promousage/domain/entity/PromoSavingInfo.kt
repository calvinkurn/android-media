package com.tokopedia.promousage.domain.entity

data class PromoSavingInfo(
    val totalSelectedPromoBenefitAmount: Double = 0.0,
    val selectedPromoCount: Int = 0,
    val message: String = "",
    val isVisible: Boolean = false
)
