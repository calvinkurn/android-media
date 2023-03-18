package com.tokopedia.dilayanitokopedia.ui.home.uimodel

data class DtShareUniversalUiModel(
    var id: String = "",
    var sharingText: String = "",
    var sharingUrl: String = "",
    var pageIdConstituents: List<String> = listOf(),
    var thumbNailTitle: String = "",
    var thumbNailImage: String = "",
    var ogImageUrl: String = "",
    var specificPageName: String = "",
    var specificPageDescription: String = "",
    var isScreenShot: Boolean = false,
    var linkerType: String = ""
)
