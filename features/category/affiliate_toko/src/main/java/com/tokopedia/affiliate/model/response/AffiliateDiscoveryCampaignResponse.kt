package com.tokopedia.affiliate.model.response

import com.google.gson.annotations.SerializedName

data class AffiliateDiscoveryCampaignResponse(

    @SerializedName("recommendedAffiliateDiscoveryCampaign")
    val recommendedAffiliateDiscoveryCampaign: RecommendedAffiliateDiscoveryCampaign? = null
) {

    data class RecommendedAffiliateDiscoveryCampaign(

        @SerializedName("data")
        val data: Data? = null,

        @SerializedName("error")
        val error: Error? = null
    ) {

        data class Data(

            @SerializedName("items")
            val items: List<Campaign?>? = null
        ) {
            data class Campaign(

                @SerializedName("additionalInformation")
                val additionalInformation: List<CampaignItem?>? = null,

                @SerializedName("appUrl")
                val appUrl: String? = null,

                @SerializedName("commission")
                val commission: Commission? = null,

                @SerializedName("title")
                val title: String? = null,

                @SerializedName("pageId")
                val pageId: Int? = null,

                @SerializedName("url")
                val url: String? = null,

                @SerializedName("imageBanner")
                val imageBanner: String? = null
            ) {

                data class Commission(

                    @SerializedName("percentage")
                    val percentage: Int? = null
                )

                data class CampaignItem(
                    @SerializedName("htmlText")
                    val htmlText: String? = null,
                    @SerializedName("type")
                    val type: Int? = null,
                    @SerializedName("color")
                    val color: String? = null
                )
            }
        }

        data class Error(

            @SerializedName("errorType")
            val errorType: Int? = null,

            @SerializedName("errorStatus")
            val errorStatus: Int? = null,

            @SerializedName("title")
            val title: String? = null,

            @SerializedName("message")
            val message: String? = null,

            @SerializedName("errorCta")
            val errorCta: List<Any?>? = null,

            @SerializedName("errorImage")
            val errorImage: ErrorImage? = null
        ) {

            data class ErrorImage(

                @SerializedName("IosURL")
                val iosURL: String? = null,

                @SerializedName("DesktopURL")
                val desktopURL: String? = null,

                @SerializedName("MobileURL")
                val mobileURL: String? = null,

                @SerializedName("AndroidURL")
                val androidURL: String? = null
            )
        }
    }
}
