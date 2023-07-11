package com.tokopedia.discovery2.data.contentCard

import com.google.gson.annotations.SerializedName
import com.tokopedia.discovery2.data.MoveAction

data class LandingPage(
    @SerializedName("header_subtitle")
    val headerSubtitle: String? = "",

    @SerializedName("url")
    val url: String? = "",

    @SerializedName("applink")
    val appLink: String? = "",

    @SerializedName("move_action")
    val moveAction: MoveAction? = null
)
