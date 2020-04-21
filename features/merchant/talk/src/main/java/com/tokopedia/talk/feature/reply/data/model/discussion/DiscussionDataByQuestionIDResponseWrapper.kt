package com.tokopedia.talk.feature.reply.data.model.discussion

import com.google.gson.annotations.SerializedName

data class DiscussionDataByQuestionIDResponseWrapper(
        @SerializedName("discussionDataByQuestionID")
        val discussionDataByQuestionID: DiscussionDataByQuestionID = DiscussionDataByQuestionID()
)