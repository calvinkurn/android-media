package com.tokopedia.promocheckout.detail.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PromoCheckoutDetailModel {

    @SerializedName("id")
    @Expose
    var id: Int = 0
    @SerializedName("expired")
    @Expose
    var expired: String? = null
    @SerializedName("real_code")
    @Expose
    var realCode: String? = null
    @SerializedName("minimum_usage")
    @Expose
    var minimumUsage: String? = null
    @SerializedName("minimum_usage_label")
    @Expose
    var minimumUsageLabel: String? = null
    @SerializedName("points")
    @Expose
    var points: Int = 0
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("catalog_title")
    @Expose
    var catalogTitle: String? = null
    @SerializedName("sub_title")
    @Expose
    var subTitle: String? = null
    @SerializedName("catalog_sub_title")
    @Expose
    var catalogSubTitle: String? = null
    @SerializedName("description")
    @Expose
    var description: String? = null
    @SerializedName("overview")
    @Expose
    var overview: String? = null
    @SerializedName("how_to_use")
    @Expose
    var howToUse: String? = null
    @SerializedName("tnc")
    @Expose
    var tnc: String? = null
    @SerializedName("icon")
    @Expose
    var icon: String? = null
    @SerializedName("thumbnail_url")
    @Expose
    var thumbnailUrl: String? = null
    @SerializedName("thumbnail_url_mobile")
    @Expose
    var thumbnailUrlMobile: String? = null
    @SerializedName("image_url")
    @Expose
    var imageUrl: String? = null
    @SerializedName("image_url_mobile")
    @Expose
    var imageUrlMobile: String? = null
    @SerializedName("thumbnail_v2_url")
    @Expose
    var thumbnailV2Url: String? = null
    @SerializedName("thumbnail_v2_url_mobile")
    @Expose
    var thumbnailV2UrlMobile: String? = null
    @SerializedName("image_v2_url")
    @Expose
    var imageV2Url: String? = null
    @SerializedName("image_v2_url_mobile")
    @Expose
    var imageV2UrlMobile: String? = null
    @SerializedName("quota")
    @Expose
    var quota: Int = 0
    @SerializedName("is_gift")
    @Expose
    var isGift: Int = 0
    @SerializedName("cta")
    @Expose
    var cta: String? = null
    @SerializedName("cta_desktop")
    @Expose
    var ctaDesktop: String? = null
    @SerializedName("usage")
    @Expose
    var usage: Usage? = null
    @SerializedName("swipe")
    @Expose
    var swipe: Swipe? = null

}
