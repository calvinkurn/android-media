package com.tokopedia.talk.feature.write.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DiscussionGetWritingFormCategory(
        @SerializedName("name")
        @Expose
        val name: String = "",
        @SerializedName("message")
        @Expose
        val message: String = ""
)