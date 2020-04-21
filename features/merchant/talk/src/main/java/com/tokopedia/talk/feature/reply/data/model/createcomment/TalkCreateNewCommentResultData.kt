package com.tokopedia.talk.feature.reply.data.model.createcomment

import com.google.gson.annotations.SerializedName

data class TalkCreateNewCommentResultData(
        @SerializedName("isSuccess")
        val isSuccess: Int = 0,
        @SerializedName("commentId")
        val commentId: Int = 0
)