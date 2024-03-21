package com.tokopedia.product.detail.common.data.model.media

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by yovi.putra on 2/20/24.
 * Copyright (c) 2024 android-tokopedia-core All rights reserved.
 */


data class LiveIndicatorData(
    @SerializedName("isLive")
    @Expose
    val isLive: Boolean = false,
    @SerializedName("channelID")
    @Expose
    val channelID: String = "",
    @SerializedName("applink")
    @Expose
    val appLink: String = ""
)
