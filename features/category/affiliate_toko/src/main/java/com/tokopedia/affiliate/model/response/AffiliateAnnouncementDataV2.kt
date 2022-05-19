package com.tokopedia.affiliate.model.response


import com.google.gson.annotations.SerializedName

data class AffiliateAnnouncementDataV2(
    @SerializedName("getAffiliateAnnouncementV2")
    var getAffiliateAnnouncementV2: GetAffiliateAnnouncementV2?
) {
    data class GetAffiliateAnnouncementV2(
        @SerializedName("Data")
        var `data`: Data?
    ) {
        data class Data(
            @SerializedName("Error")
            var error: Error?,
            @SerializedName("Status")
            var status: Int?,
            @SerializedName("TickerType")
            var type: String?,
            @SerializedName("TickerData")
            var tickerData: List<TickerData?>?
        ) {
            data class Error(
                @SerializedName("CtaLink")
                var ctaLink: CtaLink?,
                @SerializedName("CtaText")
                var ctaText: String?,
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

            data class TickerData(
                @SerializedName("AnnouncementDescription")
                var announcementDescription: String?,
                @SerializedName("AnnouncementTitle")
                var announcementTitle: String?,
                @SerializedName("CtaLink")
                var ctaLink: CtaLink?,
                @SerializedName("CtaText")
                var ctaText: String?,
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
        }
    }
}