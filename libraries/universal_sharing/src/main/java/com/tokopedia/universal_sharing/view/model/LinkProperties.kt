package com.tokopedia.universal_sharing.view.model

data class LinkProperties(
    val linkerType: String = "",
    val id: String = "",
    val ogTitle: String = "",
    val ogDescription: String = "",
    val ogImageUrl: String = "",
    val deeplink: String = "",
    val desktopUrl: String = ""
)
