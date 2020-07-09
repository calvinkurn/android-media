package com.tokopedia.product.detail.data.model.talk

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Question(
        @SerializedName("questionID")
        @Expose
        val questionID: String= "",
        @SerializedName("content")
        @Expose
        val content: String = "",
        @SerializedName("totalAnswer")
        @Expose
        val totalAnswer: Int = 0,
        @SerializedName("answer")
        @Expose
        val answer: Answer = Answer()
)