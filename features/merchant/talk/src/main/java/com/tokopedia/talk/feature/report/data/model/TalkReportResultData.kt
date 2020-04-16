package com.tokopedia.talk.feature.report.data.model

import com.google.gson.annotations.SerializedName

data class TalkReportResultData(
        @SerializedName("isSucess")
        val isSuccess: Boolean = false
)