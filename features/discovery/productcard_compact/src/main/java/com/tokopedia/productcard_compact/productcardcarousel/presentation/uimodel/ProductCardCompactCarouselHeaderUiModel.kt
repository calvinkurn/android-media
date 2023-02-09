package com.tokopedia.productcard_compact.productcardcarousel.presentation.uimodel

data class ProductCardCompactCarouselHeaderUiModel(
    val title: String = "",
    val subTitle: String = "",
    val ctaText: String = "",
    val ctaTextLink: String = "",
    val expiredTime: String = "",
    val serverTimeOffset: Long = 0,
    val backColor: String = ""
)
