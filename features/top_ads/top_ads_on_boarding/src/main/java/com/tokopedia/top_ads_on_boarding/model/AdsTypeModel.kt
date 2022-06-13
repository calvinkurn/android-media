package com.tokopedia.top_ads_on_boarding.model

data class AdsTypeModel(
    var adsTypeTitle: String = "",
    var adsTypeSubTitle: String = "",
    var adsTypeIcon: Int? = null,
    var adsTypeImage: Pair<String, String>? = null,
    var adsTypeDescription: String = "",
    var adsTypePositiveButton: String = "",
    var adsTypeNegativeButton: String = "",
    var positiveButtonLink: String = "",
    var negativeButtonLink: String = "",
    var isAdEnable: Boolean = true
)
