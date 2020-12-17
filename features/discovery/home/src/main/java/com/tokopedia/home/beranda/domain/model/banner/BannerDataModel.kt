package com.tokopedia.home.beranda.domain.model.banner

import com.google.gson.annotations.SerializedName

data class BannerDataModel(
    @SerializedName("slides")
    val slides: List<BannerSlidesModel>? = null,
    var timestamp: String = ""
)