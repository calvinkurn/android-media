package com.tokopedia.discovery2.data.productcarditem


import com.google.gson.annotations.SerializedName

data class Badges(
        @SerializedName("title")
        var title: String = "",

        @SerializedName("image_url")
        val image_url: String = "",
)