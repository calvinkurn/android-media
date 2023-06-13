package com.tokopedia.entertainment.home.adapter.viewmodel

import com.tokopedia.entertainment.home.adapter.HomeEventItem
import com.tokopedia.entertainment.home.adapter.factory.HomeTypeFactory
import com.tokopedia.entertainment.home.data.EventHomeDataResponse
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.toIntSafely

/**
 * Author errysuprayogi on 27,January,2020
 */
class EventCarouselModel(var layout: EventHomeDataResponse.Data.EventHome.Layout
                         = EventHomeDataResponse.Data.EventHome.Layout()) : HomeEventItem() {

    var items : MutableList<EventItemModel> = mutableListOf()
    var isFree = 1
    init {
        layout.items.forEach {
            items.add(EventItemModel(
                    it.id.toIntSafely(),
                    it.rating,
                    it.imageApp,
                    it.title,
                    it.location,
                    it.price,
                    it.minStartDate,
                    it.isLiked,
                    it.appUrl,
                    it.seoUrl,
                    (it.isFree == isFree && it.salesPrice.toIntSafely().isZero())
            ))
        }
    }

    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }
}