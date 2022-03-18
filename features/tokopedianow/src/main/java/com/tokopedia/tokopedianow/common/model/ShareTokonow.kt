package com.tokopedia.tokopedianow.common.model

data class ShareTokonow(
        var id: String = "",
        var sharingText: String = "",
        var sharingUrl: String = "",
        var userId: String = "",
        var pageIdConstituents: List<String> = listOf(),
        var thumbNailTitle: String = "",
        var thumbNailImage: String = "",
        var ogImageUrl: String = "",
        var specificPageName: String = "",
        var specificPageDescription: String = "",
        var isScreenShot: Boolean = false
)