package com.tokopedia.tokopedianow.home.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ValidateReferralUserResponse(
    @SerializedName("gamiReferralValidateUser")
    @Expose
    val gamiReferralValidateUser: GamiReferralValidateUser
) {
    data class GamiReferralValidateUser(
        @Expose
        @SerializedName("resultStatus")
        val resultStatus: ResultStatus,
        @Expose
        @SerializedName("status")
        val status: Int
    ) {
        data class ResultStatus(
            @Expose
            @SerializedName("code")
            val code: String,
            @Expose
            @SerializedName("message")
            val message: List<String>,
            @Expose
            @SerializedName("reason")
            val reason: String
        )
    }
}