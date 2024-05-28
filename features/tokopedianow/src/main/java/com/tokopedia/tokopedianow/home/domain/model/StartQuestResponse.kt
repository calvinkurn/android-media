package com.tokopedia.tokopedianow.home.domain.model

import com.google.gson.annotations.SerializedName

data class StartQuestResponse(
    @SerializedName("questStart")
    val response: QuestStartResponse
) {

    data class QuestStartResponse(
        @SerializedName("resultStatus")
        val resultStatus: ResultStatusResponse
    )

    data class ResultStatusResponse(
        @SerializedName("code")
        val code: String,
        @SerializedName("message")
        val message: List<String>,
        @SerializedName("status")
        val status: String
    )
}
