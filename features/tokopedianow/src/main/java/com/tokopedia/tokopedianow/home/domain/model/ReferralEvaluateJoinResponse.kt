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
        val resultStatus: ResultStatus = ResultStatus(),
        @Expose
        @SerializedName("asset")
        val asset: GamiReferralEvaluateAsset = GamiReferralEvaluateAsset(),

        @Expose
        @SerializedName("actionButton")
        val actionButton: ActionButton = ActionButton()
    ) {
        data class ResultStatus(
            @Expose
            @SerializedName("code")
            val code: String = "",
            @Expose
            @SerializedName("message")
            val message: List<String> = listOf(),
            @Expose
            @SerializedName("reason")
            val reason: String = ""
        )

        data class GamiReferralEvaluateAsset(
            @SerializedName("logoImageUrl")
            val logoImageUrl: String = "",

            @SerializedName("title")
            val title: String = "",

            @SerializedName("subtitle")
            val subtitle: String = "",

            @SerializedName("description")
            val description: String = ""
        )

        data class ActionButton(
            @SerializedName("text")
            val text: String = "",

            @SerializedName("URL")
            val url: String = "",

            @SerializedName("applink")
            val applink: String = "",

            @SerializedName("type")
            val type: String = ""
        )

        fun toHomeReceiverDialogUiModel(): HomeReceiverReferralDialogUiModel {
            return HomeReceiverReferralDialogUiModel(
                title = asset.title,
                subtitle = asset.subtitle,
                description = asset.description,
                ctaText = actionButton.text,
                statusCode = resultStatus.code,
                message = resultStatus.message.firstOrNull() ?: ""
            )
        }
    }
}
