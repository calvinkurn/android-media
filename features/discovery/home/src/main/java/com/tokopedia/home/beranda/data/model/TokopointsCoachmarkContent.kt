package com.tokopedia.home.beranda.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TokopointsCoachmarkContent(
        @Expose
        @SerializedName("title")
        val title: String = "",
        @Expose
        @SerializedName("content")
        val content: String = ""
)