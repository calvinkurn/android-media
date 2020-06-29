package com.tokopedia.talk.feature.write.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TalkCreateNewTalkResponseWrapper(
        @SerializedName("talkCreateNewTalk")
        @Expose
        val talkCreateNewTalk: TalkCreateNewTalk = TalkCreateNewTalk()
)