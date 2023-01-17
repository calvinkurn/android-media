package com.tokopedia.entertainment.home.utils

import com.tokopedia.entertainment.home.adapter.HomeEventItem
import com.tokopedia.entertainment.home.adapter.viewmodel.BannerModel
import com.tokopedia.entertainment.home.adapter.viewmodel.CategoryModel
import com.tokopedia.entertainment.home.adapter.viewmodel.EventCarouselModel
import com.tokopedia.entertainment.home.adapter.viewmodel.EventGridModel
import com.tokopedia.entertainment.home.adapter.viewmodel.EventLocationModel
import com.tokopedia.entertainment.home.adapter.viewmodel.TickerModel
import com.tokopedia.entertainment.home.data.EventHomeDataResponse
import com.tokopedia.kotlin.extensions.view.toIntSafely

object MapperHomeData {
    private const val TYPE_TICKER_DEVICE = "ANDROID"

    fun mappingItem(data: EventHomeDataResponse.Data?): MutableList<HomeEventItem> {
        val items: MutableList<HomeEventItem> = mutableListOf()
        data?.let { responseData ->

            val ticker = responseData.eventHome.ticker
            if (ticker.devices.isNotEmpty() && ticker.message.isNotEmpty()){
                if (ticker.devices.contains(TYPE_TICKER_DEVICE)){
                    items.add(TickerModel(ticker.devices, ticker.message))
                }
            }

            val layouts = responseData.eventHome.layout
            val bannerItem: EventHomeDataResponse.Data.EventHome.Layout? = layouts.find { it.id.toIntSafely() == 0 }
            bannerItem?.let { layout ->
                items.add(BannerModel(layout))
                layouts.remove(layout)
            }
            items.add(CategoryModel(data.eventChildCategory))
            layouts.let { layout ->
                layout.forEachIndexed { index, it ->
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