package com.tokopedia.tokofood.feature.home.presentation.share

data class TokoFoodHomeShare (
    var id: String = "",
    var sharingText: String = "",
    var sharingUrl: String = "",
    var sharingDeeplink: String = "",
    var thumbNailTitle: String = "",
    var thumbNailImage: String = "",
    var ogImageUrl: String = "",
    var specificPageName: String = "",
    var specificPageDescription: String = "",
    var linkerType: String = ""
)