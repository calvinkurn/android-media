package com.tokopedia.talk.feature.write.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DiscussionGetWritingForm(
        @SerializedName("maxChar")
        @Expose
        val maxChar: Int = 0,
        @SerializedName("categories")
        @Expose
        val categories: List<Any> = emptyList()
)