package com.tokopedia.talk.feature.reply.data.model.unmask

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.talk.common.data.TalkMutationResponse

data class TalkMarkNotFraudResponseWrapper(
     @SerializedName("talkMarkNotFraud")
     @Expose
     val talkMarkNotFraud: TalkMutationResponse = TalkMutationResponse()
)