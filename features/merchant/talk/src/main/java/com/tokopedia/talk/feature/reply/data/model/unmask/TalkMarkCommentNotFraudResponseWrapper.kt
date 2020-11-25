package com.tokopedia.talk.feature.reply.data.model.unmask

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.talk.common.data.TalkMutationResponse

data class TalkMarkCommentNotFraudResponseWrapper(
        @SerializedName("talkMarkCommentNotFraud")
        @Expose
        val talkMarkCommentNotFraud: TalkMutationResponse = TalkMutationResponse()
)