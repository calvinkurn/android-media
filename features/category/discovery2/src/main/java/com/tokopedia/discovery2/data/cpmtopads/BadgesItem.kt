package com.tokopedia.discovery2.data.cpmtopads

import com.google.gson.annotations.SerializedName

data class BadgesItem(

        @SerializedName("image_url")
        val imageUrl: String? = "",

        @SerializedName("show")
        val show: Boolean? = false
)