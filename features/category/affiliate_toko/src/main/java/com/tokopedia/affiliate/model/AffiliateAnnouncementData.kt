package com.tokopedia.affiliate.model

import com.google.gson.annotations.SerializedName

data class AffiliateAnnouncementData(
    @SerializedName("data")
    var `data`: Data
) {
    data class Data(
        @SerializedName("status")
        var status: Int?,
        @SerializedName("type")
        var type: String?,
        @SerializedName("announcementTitle")
        var announcementTitle: String?,
        @SerializedName("announcementDescri")
        var announcementDescri:String?,
        @SerializedName("cta_text")
        var ctaText:String?,
        @SerializedName("cta_link")
        var ctaLink:URL?,
        @SerializedName("error")
        var error:Error?
    ){
        data class Error(
            @SerializedName("message")
            var message: String?,
            @SerializedName("error_type")
            var errorType:Int?,
            @SerializedName("cta_text")
            var ctaText:String?,
            @SerializedName("cta_link")
            var ctaLink:URL?,
        )
        data class URL (
            @SerializedName("desktop")
            var desktop: String?,
            @SerializedName("mobile")
            var mobile: String?,
            @SerializedName("ios")
            var ios: String?,
            @SerializedName("android")
            var android: String?
            )
    }
}