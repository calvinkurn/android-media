package com.tokopedia.talk.feature.reply.data.model.delete.comment

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TalkDeleteCommentResultData(
        @SerializedName("isSuccess")
        @Expose
        val isSuccess: Int = 0,
        @SerializedName("commentId")
        @Expose
        val commentId: Int = 0
)