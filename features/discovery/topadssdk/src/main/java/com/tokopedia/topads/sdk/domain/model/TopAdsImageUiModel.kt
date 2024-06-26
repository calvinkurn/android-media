package com.tokopedia.topads.sdk.domain.model

data class TopAdsImageUiModel(
    var bannerId: String? = "",
    var bannerName: String = "",
    var imageUrl: String? = "",
    var imageWidth: Int = 0,
    var imageHeight: Int = 0,
    var adViewUrl: String? = "",
    var adClickUrl: String? = "",
    var nextPageToken: String? = "",
    var applink: String? = "",
    var position: Int = 0,
    var shopId: String = "",
    var currentPage: String = "",
    var kind: String = "",
    var index: Int = 0,
    var ImpressHolder: ImageShop? = null,
    var isAutoScrollEnabled: Boolean = false,
    var scrollDuration: Int = 0,
    var layoutType: String = "single"
)
