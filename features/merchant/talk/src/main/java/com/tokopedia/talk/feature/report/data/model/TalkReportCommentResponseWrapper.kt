package com.tokopedia.talk.feature.report.data.model

import com.google.gson.annotations.SerializedName

data class TalkReportCommentResponseWrapper(
        @SerializedName("talkReportComment")
        val talkReportResult: TalkReportResult = TalkReportResult()
)