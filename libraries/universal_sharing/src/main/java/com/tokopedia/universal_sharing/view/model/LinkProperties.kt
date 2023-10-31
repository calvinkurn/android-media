package com.tokopedia.universal_sharing.view.model

open class LinkProperties(
    open val linkerType: String = "",
    open val id: String = "",
    open val ogTitle: String = "",
    open val ogDescription: String = "",
    open val ogImageUrl: String = "",
    open val deeplink: String = "",
    open val desktopUrl: String = ""
)

data class LinkShareWidgetProperties(
    val page: String,
    val message: String,
    override val linkerType: String,
    override val id: String,
    override val ogTitle: String,
    override val ogDescription: String,
    override val ogImageUrl: String,
    override val deeplink: String,
    override val desktopUrl: String,
    val userId: String = "",
    ): LinkProperties()
