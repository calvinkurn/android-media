package com.tokopedia.discovery2.data

import com.google.gson.annotations.SerializedName

data class MixLeft(

    @SerializedName("banner_image_url_mobile")
    var bannerImageUrlMobile: String? = null,
    @SerializedName("banner_image_url_desktop")
    var bannerImageUrlDesktop: String? = null,
    @SerializedName("banner_supergraphic_image_url")
    var bannerSuperGraphicImage: String? = null,
    @SerializedName("background_image_url")
    var backgroundImageUrl: String? = null,
    @SerializedName("background_color")
    var backgroundColor: String? = null,
    @SerializedName("creative_name")
    var creativeName: String? = null,
    @SerializedName("url")
    var url: String? = null,
    @SerializedName("applink")
    var applink: String? = null

)
