package com.tokopedia.talk.feature.reply.data.model.delete.comment

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TalkDeleteCommentResponseWrapper(
        @SerializedName("talkDeleteComment")
        @Expose
        val talkDeleteComment: TalkDeleteComment = TalkDeleteComment()
)