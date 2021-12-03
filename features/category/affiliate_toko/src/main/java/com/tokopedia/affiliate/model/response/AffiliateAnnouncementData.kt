package com.tokopedia.affiliate.model.response

import com.google.gson.annotations.SerializedName

data class AffiliateAnnouncementData(
    @SerializedName("getAffiliateAnnouncement")
    val getAffiliateAnnouncement: GetAffiliateAnnouncement?
) {
    data class GetAffiliateAnnouncement(
        @SerializedName("Data")
        val `data`: Data?
    ) {
        data class Data(
                @SerializedName("AnnouncementDescription")
            val announcementDescription: String?,
                @SerializedName("AnnouncementTitle")
            val announcementTitle: String?,
                @SerializedName("CtaLink")
            val ctaLink: CtaLink?,
                @SerializedName("CtaText")
            val ctaText: String?,
                @SerializedName("Error")
            val error: Error?,
                @SerializedName("Status")
            val status: Int?,
                @SerializedName("Type")
            val type: String?
        ) {
            data class CtaLink(
                @SerializedName("AndroidURL")
                val androidURL: String?,
                @SerializedName("DesktopURL")
                val desktopURL: String?,
                @SerializedName("IosURL")
                val iosURL: String?,
                @SerializedName("MobileURL")
                val mobileURL: String?
            )

            data class Error(
                    @SerializedName("CtaLink")
                val ctaLink: CtaLink?,
                    @SerializedName("CtaText")
                val ctaText: String?,
                    @SerializedName("ErrorType")
                val errorType: Int?,
                    @SerializedName("Message")
                val message: String?
            )
        }
    }
}