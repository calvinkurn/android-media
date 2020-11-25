package com.tokopedia.talk.feature.reply.data.model.createcomment

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TalkCreateNewCommentResponseWrapper(
        @SerializedName("talkCreateNewComment")
        @Expose
        val talkCreateNewComment: TalkCreateNewComment = TalkCreateNewComment()
)