package com.tokopedia.talk.feature.write.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DiscussionSubmitFormResponseWrapper(
       @SerializedName("discussionSubmitForm")
       @Expose
       val discussionSubmitForm: DiscussionSubmitForm = DiscussionSubmitForm()
)