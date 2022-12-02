package com.tokopedia.discovery2.data

import com.google.gson.annotations.SerializedName

data class ThematicHeader(

    @SerializedName("title")
    val title: String? = "",

    @SerializedName("subtitle")
    val subtitle: String? = "",

    @SerializedName("color")
    val color: String? = "",

    @SerializedName("image")
    val image: String? = "",

    )
