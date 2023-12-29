package com.tokopedia.affiliate.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

class AffiliateAnnouncementDataV2(
    @SerializedName("getAffiliateAnnouncementV2")
    var getAffiliateAnnouncementV2: GetAffiliateAnnouncementV2?
) {
    data class GetAffiliateAnnouncementV2(
        @SerializedName("Data")
        var announcementData: Data?
    ) {
        data class Data(
            @SerializedName("Error")
            var error: Error?,
            @SerializedName("Id")
            var id: Long?,
            @SerializedName("Status")
            var status: Int?,
            @SerializedName("TickerType")
            var type: String?,
            @SerializedName("TickerSubType")
            var subType: String?,
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

            @Parcelize
            data class TickerData(
                @SerializedName("AnnouncementDescription")
                var announcementDescription: String?,
                @SerializedName("AnnouncementTitle")
                var announcementTitle: String?,
                @SerializedName("CtaLink")
                var ctaLink: CtaLink?,
                @SerializedName("CtaText")
                var ctaText: String?,
                @SerializedName("CtaLinkSecondary")
                var ctaLinkSecondary: CtaLink?,
                @SerializedName("CtaTextSecondary")
                var ctaTextSecondary: String?,
                @SerializedName("IllustrationURL")
                var illustrationURL: String?
            ) : Parcelable {
                @Parcelize
                data class CtaLink(
                    @SerializedName("AndroidURL")
                    var androidURL: String?,
                    @SerializedName("DesktopURL")
                    var desktopURL: String?,
                    @SerializedName("IosURL")
                    var iosURL: String?,
                    @SerializedName("MobileURL")
                    var mobileURL: String?
                ) : Parcelable
            }
        }
    }
}
