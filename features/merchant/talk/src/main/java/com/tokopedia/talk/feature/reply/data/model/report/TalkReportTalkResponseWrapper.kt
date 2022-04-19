package com.tokopedia.talk.feature.reply.data.model.report

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.talk.common.data.TalkMutationResponse

data class TalkReportTalkResponseWrapper(
        @SerializedName("talkReportTalk")
        @Expose
        val talkReportTalk: TalkMutationResponse = TalkMutationResponse()
)