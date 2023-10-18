package com.tokopedia.tokopedianow.common.model

import com.tokopedia.kotlin.extensions.view.EMPTY

data class TokoNowDynamicHeaderUiModel(
    val title: String = "",
    val subTitle: String = "",
    val ctaText: String = "",
    val ctaTextLink: String = "",
    val expiredTime: String = "",
    val serverTimeOffset: Long = 0,
    val backColor: String = "",
    val circleSeeAll: Boolean = false,
    val widgetId: String = String.EMPTY
)
