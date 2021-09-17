package com.tokopedia.affiliate.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class AffiliateValidateUserData(
        @SerializedName("validateAffiliateUserStatus")
        var validateAffiliateUserStatus: ValidateAffiliateUserStatus
) {
    @Keep
    data class ValidateAffiliateUserStatus(
            @SerializedName("Data")
            var `data`: Data?
    ) {
        @Keep
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
            @Keep
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

                @Keep
                class CtaLink(
                        @SerializedName("DesktopURL") val desktopUrl : String?,
                        @SerializedName("MobileURL") val mobileUrl : String?,
                        @SerializedName("AndroidURL") val androidUrl : String?,
                        @SerializedName("IosURL") val iOSUrl : String?
                )
            }
        }
    }
}