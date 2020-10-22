package com.tokopedia.talk.feature.write.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.talk.feature.write.data.model.DiscussionGetWritingForm

data class DiscussionGetWritingFormResponseWrapper(
        @SerializedName("discussionGetWritingForm")
        @Expose
        val discussionGetWritingForm: DiscussionGetWritingForm = DiscussionGetWritingForm()
)