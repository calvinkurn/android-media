package com.tokopedia.brandlist.brandlist_page.data.model

import com.google.gson.annotations.SerializedName

data class Header(
        @SerializedName("title")
        val title: String = "",
        @SerializedName("ctaText")
        val ctaText: String = "",
        @SerializedName("link")
        val link: String = ""
)