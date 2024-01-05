package com.tokopedia.affiliate.model.response

import com.google.gson.annotations.SerializedName

data class AffiliateTrafficCommissionCardDetails(
    @SerializedName("getAffiliateTrafficCommissionDetailCards")
    var getAffiliateTrafficCommissionDetailCards: GetAffiliateTrafficCommissionDetailCards?
) {
    data class GetAffiliateTrafficCommissionDetailCards(
        @SerializedName("Data")
        var trafficCommissionData: Data?
    ) {
        data class Data(
            @SerializedName("Error")
            var error: Error?,
            @SerializedName("HasNext")
            var hasNext: Boolean?,
            @SerializedName("LastID")
            var lastID: String?,
            @SerializedName("Status")
            var status: Int?,
            @SerializedName("TrafficCommissionCardDetail")
            var trafficCommissionCardDetail: List<TrafficCommissionCardDetail?>?
        ) {
            data class Error(
                @SerializedName("CtaLink")
                var ctaLink: CtaLink?,
                @SerializedName("CtaText")
                var ctaText: String?,
                @SerializedName("ErrorType")
                var errorType: Int?,
                @SerializedName("Message")
                var message: String?
            ) {
                data class CtaLink(
                    @SerializedName("AndroidURL")
                    var androidURL: String?,
                    @SerializedName("DesktopURL")
                    var desktopURL: String?,
                    @SerializedName("IosURL")
                    var iosURL: String?,
                    @SerializedName("MobileURL")
                    var mobileURL: String?
                )
            }
            data class TrafficCommissionCardDetail(
                @SerializedName("CardDescription")
                var cardDescription: String?,
                @SerializedName("CardTitle")
                var cardTitle: String?,
                @SerializedName("Image")
                var image: Image?
            ) {
                data class Image(
                    @SerializedName("AndroidURL")
                    var androidURL: String?,
                    @SerializedName("DesktopURL")
                    var desktopURL: String?,
                    @SerializedName("IosURL")
                    var iosURL: String?,
                    @SerializedName("MobileURL")
                    var mobileURL: String?
                )
            }
        }
    }
}
