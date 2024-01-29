package com.tokopedia.recommendation_widget_common.widget.foryou.topads

interface BannerTopAdsListener {

    fun onBannerTopAdsImpress(model: BannerTopAdsModel, position: Int)
    fun onBannerTopAdsClick(model: BannerTopAdsModel, position: Int)

    fun onBannerTopAdsOldImpress(model: BannerOldTopAdsModel, position: Int)
    fun onBannerTopAdsOldClick(model: BannerOldTopAdsModel, position: Int)
}
