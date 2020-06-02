package com.tokopedia.product.detail.data.model.talk

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DiscussionMostHelpful(
        @SerializedName("totalQuestion")
        @Expose
        val totalQuestion: Int = 0,
        @SerializedName("question")
        @Expose
        val questions: List<Question>? = null
)