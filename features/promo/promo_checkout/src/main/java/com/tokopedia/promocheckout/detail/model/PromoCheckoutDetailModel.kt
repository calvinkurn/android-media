package com.tokopedia.promocheckout.detail.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PromoCheckoutDetailModel {

    @SerializedName("id")
    @Expose
    var id: Int = 0
    @SerializedName("expired")
    @Expose
    var expired: String? = ""
    @SerializedName("real_code")
    @Expose
    var realCode: String? = ""
    @SerializedName("minimum_usage")
    @Expose
    var minimumUsage: String? = ""
    @SerializedName("minimum_usage_label")
    @Expose
    var minimumUsageLabel: String? = ""
    @SerializedName("points")
    @Expose
    var points: Int = 0
    @SerializedName("title")
    @Expose
    var title: String? = ""
    @SerializedName("catalog_title")
    @Expose
    var catalogTitle: String? = ""
    @SerializedName("sub_title")
    @Expose
    var subTitle: String? = ""
    @SerializedName("catalog_sub_title")
    @Expose
    var catalogSubTitle: String? = ""
    @SerializedName("description")
    @Expose
    var description: String? = ""
    @SerializedName("overview")
    @Expose
    var overview: String? = ""
    @SerializedName("how_to_use")
    @Expose
    var howToUse: String? = ""
    @SerializedName("tnc")
    @Expose
    var tnc: String? = ""
    @SerializedName("icon")
    @Expose
    var icon: String? = ""
    @SerializedName("thumbnail_url")
    @Expose
    var thumbnailUrl: String? = ""
    @SerializedName("thumbnail_url_mobile")
    @Expose
    var thumbnailUrlMobile: String? = ""
    @SerializedName("image_url")
    @Expose
    var imageUrl: String? = ""
    @SerializedName("image_url_mobile")
    @Expose
    var imageUrlMobile: String? = ""
    @SerializedName("thumbnail_v2_url")
    @Expose
    var thumbnailV2Url: String? = ""
    @SerializedName("thumbnail_v2_url_mobile")
    @Expose
    var thumbnailV2UrlMobile: String? = ""
    @SerializedName("image_v2_url")
    @Expose
    var imageV2Url: String? = null
    @SerializedName("image_v2_url_mobile")
    @Expose
    var imageV2UrlMobile: String? = ""
    @SerializedName("quota")
    @Expose
    var quota: Int = 0
    @SerializedName("is_gift")
    @Expose
    var isGift: Int = 0
    @SerializedName("cta")
    @Expose
    var cta: String? = ""
    @SerializedName("cta_desktop")
    @Expose
    var ctaDesktop: String? = ""
    @SerializedName("usage")
    @Expose
    var usage: Usage? = Usage()
    @SerializedName("swipe")
    @Expose
    var swipe: Swipe? = Swipe()

}
