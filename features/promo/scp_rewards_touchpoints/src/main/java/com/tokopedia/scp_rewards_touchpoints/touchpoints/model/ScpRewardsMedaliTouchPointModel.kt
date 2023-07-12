package com.tokopedia.scp_rewards_touchpoints.touchpoints.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO

data class ScpRewardsMedaliTouchPointModel(
     @SerializedName("scpRewardsMedaliTouchpointOrder")
     val scpRewardsMedaliTouchpointOrder: ScpRewardsMedaliTouchpointOrder = ScpRewardsMedaliTouchpointOrder()
){
    data class ScpRewardsMedaliTouchpointOrder(
        @SerializedName("resultStatus")
        val resultStatus: ResultStatus = ResultStatus(),

        @SerializedName("medaliTouchpointOrder")
        val medaliTouchpointOrder: MedaliTouchpointOrder = MedaliTouchpointOrder(),

        @SerializedName("isShown")
        val isShown: Boolean = false
    ) {

        data class ResultStatus(
            @SerializedName("code")
            val code: String = String.EMPTY,

            @SerializedName("message")
            val message: List<String> = emptyList(),

            @SerializedName("status")
            val status: String = String.EMPTY
        )

        data class MedaliTouchpointOrder(
            @SerializedName("cta")
            val cta: CtaItem = CtaItem(),

            @SerializedName("medaliIconImageURL")
            val medaliIconImageURL: String = String.EMPTY,

            @SerializedName("medaliSlug")
            val medaliSlug: String = String.EMPTY,

            @SerializedName("medaliID")
            val medaliID: Int = Int.ZERO,

            @SerializedName("medaliSunburstImageURL")
            val medaliSunburstImageURL: String = String.EMPTY,

            @SerializedName("infoMessage")
            val infoMessage: InfoMessage = InfoMessage(),

            @SerializedName("retryChecking")
            val retryChecking: RetryChecking = RetryChecking()
        ) {

            data class CtaItem(
                @SerializedName("appLink")
                val appLink: String = String.EMPTY,

                @SerializedName("isShown")
                val isShown: Boolean = false,

                @SerializedName("text")
                val text: String = String.EMPTY,

                @SerializedName("url")
                val url: String = String.EMPTY
            )

            data class InfoMessage(
                @SerializedName("subtitle")
                val subtitle: String = String.EMPTY,

                @SerializedName("title")
                val title: String = String.EMPTY
            )

            data class RetryChecking(
                @SerializedName("isRetryable")
                val isRetryable: Boolean = false,

                @SerializedName("durationToRetry")
                val durationToRetry: Long = 0
            )
        }
    }
}
