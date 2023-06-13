package com.tokopedia.topads.sdk.utils

import com.tokopedia.topads.sdk.TopAdsConstants.TdnBannerConstants.TYPE_CAROUSEL
import com.tokopedia.topads.sdk.TopAdsConstants.TdnBannerConstants.TYPE_SINGLE
import com.tokopedia.topads.sdk.TopAdsConstants.TdnBannerConstants.TYPE_VERTICAL_CAROUSEL
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel

object TdnHelper {
    private val categorisedList = mutableListOf<List<TopAdsImageViewModel>>()

    fun categoriesTdnBanners(banners: List<TopAdsImageViewModel>): MutableList<List<TopAdsImageViewModel>> {
        categorisedList.clear()
        val carouselList = mutableListOf<TopAdsImageViewModel>()
        val singleList = mutableListOf<TopAdsImageViewModel>()
        val verticalList = mutableListOf<TopAdsImageViewModel>()
        banners.forEach {
            when {
                it.layoutType == TYPE_CAROUSEL -> {
                    carouselList.add(it)
                }
                it.layoutType == TYPE_VERTICAL_CAROUSEL -> {
                    verticalList.add(it)
                }
                it.layoutType == TYPE_SINGLE || it.layoutType.isEmpty() -> {
                    singleList.add(it)
                }
            }
        }
        if (carouselList.isNotEmpty()) categorisedList.add(carouselList)
        if (singleList.isNotEmpty()) categorisedList.add(singleList)
        if (verticalList.isNotEmpty()) categorisedList.add(verticalList)
        return categorisedList
    }
}
