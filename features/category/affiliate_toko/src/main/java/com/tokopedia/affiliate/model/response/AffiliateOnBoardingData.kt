package com.tokopedia.affiliate.model.response


import com.google.gson.annotations.SerializedName

data class AffiliateOnBoardingData(
        @SerializedName("onboardAffiliate")
        var onBoardAffiliate: OnBoardAffiliate?
) {
    data class OnBoardAffiliate(
            @SerializedName("data")
            var `data`: Data?
    ) {
        data class Data(
                @SerializedName("error")
                var error: Error?,
                @SerializedName("status")
                var status: Int?
        ) {
            class Error(
                    @SerializedName("errorType")
                    var errorType: Int?,
                    @SerializedName("message")
                    var message: String?,
                    @SerializedName("ctaText")
                    var ctaText: String?,
                    @SerializedName("ctaLink")
                    var ctaLink : CtaLink
            ) {

                class CtaLink(
                        @SerializedName("desktopURL") val desktopUrl : String?,
                )
            }
        }
    }
}