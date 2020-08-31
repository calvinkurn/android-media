package com.tokopedia.talk.feature.reply.data.model.createcomment

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TalkCreateNewCommentResultData(
        @SerializedName("isSuccess")
        @Expose
        val isSuccess: Int = 0,
        @SerializedName("commentId")
        @Expose
        val commentId: Int = 0
)