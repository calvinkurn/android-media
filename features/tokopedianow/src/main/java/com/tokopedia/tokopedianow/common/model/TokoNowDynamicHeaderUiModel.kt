package com.tokopedia.tokopedianow.common.model

data class TokoNowDynamicHeaderUiModel(
    val title: String = "",
    val subTitle: String = "",
    val ctaText: String = "",
    val ctaTextLink: String = "",
    val expiredTime: String = "",
    val serverTimeOffset: Long = 0,
    val backColor: String = ""
)