package com.tokopedia.product.manage.feature.list.view.model

sealed class TopAdsPage {
    object OnBoarding: TopAdsPage()
    object ManualAds: TopAdsPage()
    object AutoAds: TopAdsPage()
}