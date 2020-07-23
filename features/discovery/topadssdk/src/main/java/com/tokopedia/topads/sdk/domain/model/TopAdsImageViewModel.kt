package com.tokopedia.topads.sdk.domain.model

data class TopAdsImageViewModel(
        var imageUrl: String? = "",
        var imageWidth: Int = 0,
        var imageHeight: Int = 0,
        var adViewUrl: String? = "",
        var adClickUrl: String? = "",
        var nextPageToken: String? = "",
        var applink: String? = ""
)