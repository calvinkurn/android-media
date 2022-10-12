package com.tokopedia.tokopoints.view.model.section

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LayoutBannerAttr(
    @SerializedName("bannerType")
    @Expose
    var bannerType: String? = null,
    @SerializedName("imageList")
    @Expose
    var imageList: List<ImageList?>? = null,
)
