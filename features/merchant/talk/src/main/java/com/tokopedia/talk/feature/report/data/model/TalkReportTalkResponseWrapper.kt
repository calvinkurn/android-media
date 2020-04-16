package com.tokopedia.talk.feature.report.data.model

import com.google.gson.annotations.SerializedName

data class TalkReportTalkResponseWrapper(
        @SerializedName("talkReportTalk")
        val talkReportResult: TalkReportResult = TalkReportResult()
)