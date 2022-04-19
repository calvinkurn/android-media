package com.tokopedia.entertainment.home.utils

import com.tokopedia.entertainment.home.adapter.HomeEventItem
import com.tokopedia.entertainment.home.adapter.viewmodel.*
import com.tokopedia.entertainment.home.data.EventHomeDataResponse
import com.tokopedia.kotlin.extensions.view.toIntOrZero

object MapperHomeData {
    fun mappingItem(data: EventHomeDataResponse.Data?): MutableList<HomeEventItem> {
        val items: MutableList<HomeEventItem> = mutableListOf()
        data?.let {
            val layouts = it.eventHome.layout
            val bannerItem: EventHomeDataResponse.Data.EventHome.Layout? = layouts.find { it.id.toIntOrZero() == 0 }
            bannerItem?.let {
                items.add(BannerModel(it))
                layouts.remove(it)
            }
            items.add(CategoryModel(data.eventChildCategory))
            layouts.let {
                it.forEachIndexed { index, it ->
                    if (index == 2) {
                        items.add(EventLocationModel(data.eventLocationSearch))
                    }
                    if (it.isCard == 1) {
                        items.add(EventCarouselModel(it))
                    } else {
                        items.add(EventGridModel(it))
                    }
                }
            }
        }
        return items
    }
}