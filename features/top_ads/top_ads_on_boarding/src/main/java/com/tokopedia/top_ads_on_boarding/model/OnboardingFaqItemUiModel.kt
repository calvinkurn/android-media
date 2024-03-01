package com.tokopedia.top_ads_on_boarding.model

data class OnboardingFaqItemUiModel(
    val id: Int,
    val title: String,
    val desc: String,
    var isExpanded: Boolean
)
