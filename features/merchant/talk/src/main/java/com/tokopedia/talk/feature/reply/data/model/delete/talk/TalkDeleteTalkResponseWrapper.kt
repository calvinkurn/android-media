package com.tokopedia.talk.feature.reply.data.model.delete.talk

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TalkDeleteTalkResponseWrapper(
        @SerializedName("talkDeleteTalk")
        @Expose
        val talkDeleteTalk: TalkDeleteTalk = TalkDeleteTalk()
)