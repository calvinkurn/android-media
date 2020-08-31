package com.tokopedia.discovery2.data.cpmtopads

import com.google.gson.annotations.SerializedName


data class Headline(

        @SerializedName("badges")
        val badges: List<BadgesItem?>? = null,

        @SerializedName("image")
        val image: Image? = null,

        @SerializedName("shop")
        val shop: Shop? = null,

        @SerializedName("promoted_text")
        val promotedText: String? = "",

        @SerializedName("name")
        val name: String? = "",

        @SerializedName("button_text")
        val buttonText: String? = "",

        @SerializedName("uri")
        val uri: String? = ""
)