package com.tokopedia.topads.sdk.domain.model

data class TopAdsImageViewModel(
        var bannerId: String? = "",
        var bannerName: String = "",
        var imageUrl: String? = "",
        var imageWidth: Int = 0,
        var imageHeight: Int = 0,
        var adViewUrl: String? = "",
        var adClickUrl: String? = "",
        var nextPageToken: String? = "",
        var applink: String? = "",
        var position: Int = 0
)