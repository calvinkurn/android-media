package com.tokopedia.tokopedianow.home.domain.model

data class ShareHomeTokonow(
        val sharingText: String = "",
        val sharingUrl: String = "",
        val userId: String = "",
        val pageType: String = "",
        val thumbNailTitle: String = "",
        val thumbNailImage: String = "",
        val ogImageUrl: String = "",
        val specificPageName: String = "",
        val specificPageDescription: String = "",
)