package com.tokopedia.tokopoints.view.model.section

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ImageList(
    @SerializedName("imageURL")
    @Expose
    var imageURL: String = "",
    @SerializedName("imageURLMobile")
    @Expose
    var imageURLMobile: String = "",
    @SerializedName("redirectURL")
    @Expose
    var redirectURL: String = "",
    @SerializedName("redirectAppLink")
    @Expose
    var redirectAppLink: String = "",
    @SerializedName("title")
    @Expose
     var title: String = "",
    @SerializedName("subTitle")
    @Expose
    var subTitle: String = "",
    @SerializedName("inBannerTitle")
    @Expose
    var inBannerTitle: String = "",
    @SerializedName("inBannerSubTitle")
    @Expose
    var inBannerSubTitle: String = "",
)
