package com.tokopedia.talk.feature.reply.data.model.delete.comment

import com.google.gson.annotations.SerializedName

data class TalkDeleteCommentResponseWrapper(
        @SerializedName("talkDeleteComment")
        val talkDeleteComment: TalkDeleteComment = TalkDeleteComment()
)