package com.tokopedia.interestpick.data.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Header(
        @SerializedName("image_url")
        @Expose
        val imageUrl: String = "",

        @SerializedName("description")
        @Expose
        val description: String = "",

        @SerializedName("title")
        @Expose
        val title: String = ""
)