package com.tokopedia.tokomart.searchcategory.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.home_component.data.DynamicHomeChannelCommon

data class DynamicChannelModel(
    @SerializedName("dynamicHomeChannel")
    @Expose
    val dynamicHomeChannelCommon: DynamicHomeChannelCommon = DynamicHomeChannelCommon(),
)