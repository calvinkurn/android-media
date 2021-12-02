package com.tokopedia.affiliate.model.response


import com.google.gson.annotations.SerializedName

data class AffiliateValidateUserData(
        @SerializedName("validateAffiliateUserStatus")
        var validateAffiliateUserStatus: ValidateAffiliateUserStatus
) {
    data class ValidateAffiliateUserStatus(
            @SerializedName("Data")
            var `data`: Data?
    ) {
        data class Data(
                @SerializedName("Error")
                var error: Error?,
                @SerializedName("IsEligible")
                var isEligible: Boolean?,
                @SerializedName("IsRegistered")
                var isRegistered: Boolean?,
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