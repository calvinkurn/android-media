package com.tokopedia.topads.sdk.utils

import com.tokopedia.topads.sdk.TopAdsConstants.TdnBannerConstants.TYPE_CAROUSEL
import com.tokopedia.topads.sdk.TopAdsConstants.TdnBannerConstants.TYPE_SINGLE
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel

object TdnHelper {
    private val categorisedList = mutableListOf<List<TopAdsImageViewModel>>()

    fun categoriesTdnBanners(banners: List<TopAdsImageViewModel>): MutableList<List<TopAdsImageViewModel>> {
        categorisedList.clear()
        val carouselList = mutableListOf<TopAdsImageViewModel>()
        val singleList = mutableListOf<TopAdsImageViewModel>()
        banners.forEach {
            if (it.layoutType == TYPE_CAROUSEL) {
                carouselList.add(it)
            } else if (it.layoutType == TYPE_SINGLE || it.layoutType.isEmpty()) {
                singleList.add(it)
            }
        }
        if (carouselList.isNotEmpty()) categorisedList.add(carouselList)
        if (singleList.isNotEmpty()) categorisedList.add(singleList)
        return categorisedList
    }
}
