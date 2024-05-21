package com.tokopedia.recommendation_widget_common.infinite.foryou.topads

import com.tokopedia.recommendation_widget_common.infinite.foryou.topads.model.BannerTopAdsModel
import com.tokopedia.topads.sdk.domain.model.CpmData

interface BannerTopAdsListener {

    // Headline TopAds
    fun onBannerAdsClicked(position: Int, applink: String?, data: CpmData?)

    // TopAds
    fun onBannerTopAdsImpress(model: BannerTopAdsModel, position: Int)
    fun onBannerTopAdsClick(model: BannerTopAdsModel, position: Int)
}
