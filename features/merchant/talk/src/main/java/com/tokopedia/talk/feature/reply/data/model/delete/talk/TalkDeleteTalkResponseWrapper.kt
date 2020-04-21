package com.tokopedia.talk.feature.reply.data.model.delete.talk

import com.google.gson.annotations.SerializedName

data class TalkDeleteTalkResponseWrapper(
        @SerializedName("talkDeleteTalk")
        val talkDeleteTalk: TalkDeleteTalk = TalkDeleteTalk()
)