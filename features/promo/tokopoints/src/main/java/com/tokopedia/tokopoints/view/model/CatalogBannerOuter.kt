package com.tokopedia.tokopoints.view.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CatalogBannerOuter(
    @Expose
    @SerializedName("banners")
    var bannerData: CatalogBannerBase = CatalogBannerBase()
)
