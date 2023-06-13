package com.tokopedia.tokopoints.view.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CatalogBannerBase(
    @Expose
    @SerializedName("slides")
    var banners: List<CatalogBanner> = listOf()
)
