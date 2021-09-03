package com.tokopedia.affiliate.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class AffiliateValidateUserData(
        @SerializedName("validateUserStatus")
        var validateUserStatus: ValidateUserStatus
) {
    @Keep
    data class ValidateUserStatus(
            @SerializedName("data")
            var `data`: Data
    ) {
        @Keep
        data class Data(
                @SerializedName("error")
                var error: Error,
                @SerializedName("isEligible")
                var isEligible: Boolean,
                @SerializedName("isRegistered")
                var isRegistered: Boolean,
                @SerializedName("status")
                var status: Boolean
        ) {
            @Keep
            class Error
        }
    }
}