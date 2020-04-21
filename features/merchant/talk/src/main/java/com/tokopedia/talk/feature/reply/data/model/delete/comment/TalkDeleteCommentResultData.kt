package com.tokopedia.talk.feature.reply.data.model.delete.comment

import com.google.gson.annotations.SerializedName

data class TalkDeleteCommentResultData(
        @SerializedName("isSuccess")
        val isSuccess: Int = 0,
        @SerializedName("commentId")
        val commentId: Int = 0
)