package com.tokopedia.talk.feature.write.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DiscussionSubmitForm(
        @SerializedName("discussionID")
        @Expose
        val discussionId: String = ""
)