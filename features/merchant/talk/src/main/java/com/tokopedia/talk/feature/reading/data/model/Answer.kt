package com.tokopedia.talk.feature.reading.data.model

import com.google.gson.annotations.SerializedName

data class Answer(
        @SerializedName("content")
        val content: String = "",
        @SerializedName("userName")
        val userName: String = "",
        @SerializedName("createTime")
        val createTime: Int = 0,
        @SerializedName("createTimeFormatted")
        val createTimeFormatted: String = "",
        @SerializedName("attachedProductCount")
        val attachedProductCount: Int = 0
)