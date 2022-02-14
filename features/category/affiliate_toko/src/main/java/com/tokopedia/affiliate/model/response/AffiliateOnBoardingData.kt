package com.tokopedia.affiliate.model.response


import com.google.gson.annotations.SerializedName

data class AffiliateOnBoardingData(
        @SerializedName("onBoardAffiliate")
        var onBoardAffiliate: OnBoardAffiliate?
) {
    data class OnBoardAffiliate(
            @SerializedName("Data")
            var `data`: Data?
    ) {
        data class Data(
                @SerializedName("Error")
                var error: Error?,
                @SerializedName("Status")
                var status: Int?
        ) {
            class Error(
                    @SerializedName("ErrorType")
                    var errorType: Int?,
                    @SerializedName("Message")
                    var message: String?,
                    @SerializedName("CtaText")
                    var ctaText: String?,
                    @SerializedName("CtaLink")
                    var ctaLink : CtaLink
            ) {

                class CtaLink(
                        @SerializedName("AndroidURL") val androidUrl : String?,
                )
            }
        }
    }
}