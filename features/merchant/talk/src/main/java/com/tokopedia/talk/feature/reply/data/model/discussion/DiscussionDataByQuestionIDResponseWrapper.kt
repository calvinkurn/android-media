package com.tokopedia.talk.feature.reply.data.model.discussion

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DiscussionDataByQuestionIDResponseWrapper(
        @SerializedName("discussionDataByQuestionID")
        @Expose
        val discussionDataByQuestionID: DiscussionDataByQuestionID = DiscussionDataByQuestionID()
)