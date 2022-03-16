package com.tokopedia.tokopedianow.home.domain.model

data class ShareHomeTokonow(
        val sharingText: String = "",
        val sharingUrl: String = "",
        val userId: String = "",
        val pageIdConstituents: List<String> = listOf(),
        val thumbNailTitle: String = "",
        val thumbNailImage: String = "",
        val ogImageUrl: String = "",
        val specificPageName: String = "",
        val specificPageDescription: String = "",
)