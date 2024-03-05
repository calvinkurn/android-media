package com.tokopedia.product.detail.view.viewholder.media.model

import com.tokopedia.product.detail.common.data.model.media.LiveIndicatorData

/**
 * Created by yovi.putra on 2/20/24.
 * Copyright (c) 2024 android-tokopedia-core All rights reserved.
 */

data class LiveIndicatorUiModel(
    val isLive: Boolean = false,
    val channelID: String = "",
    val appLink: String = ""
)

fun LiveIndicatorData.asUiModel() = LiveIndicatorUiModel(
    isLive = isLive,
    channelID = channelID,
    appLink = appLink
)
