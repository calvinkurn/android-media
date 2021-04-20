package com.tokopedia.talk.feature.reply.data.model.report

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.talk.common.data.TalkMutationResponse

data class TalkReportCommentResponseWrapper(
        @SerializedName("talkReportComment")
        @Expose
        val talkReportComment: TalkMutationResponse = TalkMutationResponse()
)