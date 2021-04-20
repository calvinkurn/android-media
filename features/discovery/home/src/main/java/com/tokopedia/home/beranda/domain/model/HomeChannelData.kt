package com.tokopedia.home.beranda.domain.model

import com.google.gson.annotations.SerializedName

data class HomeChannelData(
    @SerializedName("dynamicHomeChannel")
    val dynamicHomeChannel: DynamicHomeChannel = DynamicHomeChannel()
)