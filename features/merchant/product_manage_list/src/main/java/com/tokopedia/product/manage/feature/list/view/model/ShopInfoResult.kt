package com.tokopedia.product.manage.feature.list.view.model

data class ShopInfoResult(
    val shopDomain: String,
    val isGoldMerchant: Boolean,
    val isOfficialStore: Boolean,
    val topAds: TopAdsInfo
)