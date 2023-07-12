package com.tokopedia.promousage.view.model

data class PromoItemUiModel(
    val id: String = "",

    val index: Int = 0,
    val code: String = "",
    val shopId: String = "",
    val title: String = "",
    val benefitTypeStr: String = "",
    val benefitAmountStr: String = "",
    val benefitDetails: List<PromoBenefitDetailUiModel> = emptyList(),
    val cardDetails: List<PromoCardDetailUiModel> = emptyList(),
    val promoInfos: List<PromoInfoUiModel> = emptyList(),

    val isSelected: Boolean = false,
    val isRecommended: Boolean = false,
    val isEligible: Boolean = false,
    val isAttempted: Boolean = false,
    val isHighlighted: Boolean = false,
)
