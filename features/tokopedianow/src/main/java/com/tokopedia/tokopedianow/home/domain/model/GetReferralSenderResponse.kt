package com.tokopedia.tokopedianow.home.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetReferralSenderHomeResponse(
    @SerializedName("gamiReferralSenderHome")
    @Expose
    val gamiReferralSenderHome: GamiReferralSenderHome
) {
    data class GamiReferralSenderHome(
        @Expose
        @SerializedName("resultStatus")
        val resultStatus: ResultStatus,
        @Expose
        @SerializedName("status")
        val sharingMetaData: SharingMetaData
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
        data class SharingMetaData(
            @Expose
            @SerializedName("sharingURL")
            val sharingUrl: String
        )
    }
}