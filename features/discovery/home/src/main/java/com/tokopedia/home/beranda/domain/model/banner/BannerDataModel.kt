package com.tokopedia.home.beranda.domain.model.banner

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.home_component.model.AtfContent

data class BannerDataModel(
    @SerializedName("slides", alternate = ["banners"])
    @Expose
    val slides: List<BannerSlidesModel>? = null,
    @SerializedName("timestamp")
    @Expose
    var timestamp: String = ""
): AtfContent
