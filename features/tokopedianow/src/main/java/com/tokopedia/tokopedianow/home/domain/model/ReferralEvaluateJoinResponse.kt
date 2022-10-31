package com.tokopedia.tokopedianow.home.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeReceiverReferralDialogUiModel

data class ReferralEvaluateJoinResponse(
    @SerializedName("gamiReferralEvaluateJoin")
    val gamiReferralEvaluteJoinResponse: GamiReferralEvaluateJoinResponse
) {
    data class GamiReferralEvaluateJoinResponse(
        @Expose
        @SerializedName("resultStatus")
        val resultStatus: ResultStatus,
        @Expose
        @SerializedName("asset")
        val asset: GamiReferralEvaluateAsset
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

        data class GamiReferralEvaluateAsset(
            @SerializedName("logoImageUrl")
            val logoImageUrl: String,

            @SerializedName("title")
            val title: String,

            @SerializedName("subtitle")
            val subtitle: String,

            @SerializedName("description")
            val description: String
        ) {
            fun toHomeReceiverDialogUiModel(): HomeReceiverReferralDialogUiModel {
                return HomeReceiverReferralDialogUiModel(title = this.title, subtitle = this.subtitle, description = this.subtitle)
            }
        }
    }
}
