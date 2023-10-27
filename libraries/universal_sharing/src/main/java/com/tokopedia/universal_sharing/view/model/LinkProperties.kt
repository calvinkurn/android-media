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

data class LinkShareWidgetProperties(
    val page: String,
    val deeplink: String,
    val desktopUrl: String,
    val linkerType: String,
    val id: String,
    val userId: String = "0",
    val ogTitle: String = "",
    val ogDescription: String = "",
    val ogImageUrl: String = "",
)
