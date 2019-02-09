package com.tokopedia.product.detail.data.model.talk

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TalkResponse(
        @SerializedName("talk_comment")
        @Expose
        val talkComment: List<Talk> = listOf(),

        @SerializedName("talk_id")
        @Expose
        val talkId: String = ""
)