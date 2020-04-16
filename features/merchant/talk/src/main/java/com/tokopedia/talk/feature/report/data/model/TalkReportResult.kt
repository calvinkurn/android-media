package com.tokopedia.talk.feature.report.data.model

import com.google.gson.annotations.SerializedName

data class TalkReportResult(
        @SerializedName("status")
        val status: String = "",
        @SerializedName("messageError")
        val messageError: String = "",
        @SerializedName("data")
        val data: TalkReportResultData = TalkReportResultData(),
        @SerializedName("messageErrorOriginal")
        val originalErrorMessage: String = ""
)