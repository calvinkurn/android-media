package com.tokopedia.home.beranda.domain.model

import com.google.gson.annotations.SerializedName

data class HomeChannelData(
    @SerializedName("dynamicHomeChannel", alternate = ["getHomeChannelV2"])
    val dynamicHomeChannel: DynamicHomeChannel = DynamicHomeChannel()
)