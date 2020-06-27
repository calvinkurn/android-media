package com.tokopedia.talk.feature.write.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DiscussionGetWritingFormResponseWrapper(
        @SerializedName("discussionGetWritingForm")
        @Expose
        val discussionGetWritingForm: DiscussionGetWritingForm = DiscussionGetWritingForm()
)