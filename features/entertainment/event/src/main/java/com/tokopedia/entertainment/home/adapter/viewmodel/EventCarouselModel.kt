package com.tokopedia.entertainment.home.adapter.viewmodel

import com.tokopedia.entertainment.home.adapter.HomeEventItem
import com.tokopedia.entertainment.home.adapter.factory.HomeTypeFactory
import com.tokopedia.entertainment.home.data.EventHomeDataResponse

/**
 * Author errysuprayogi on 27,January,2020
 */
class EventCarouselModel(var layout: EventHomeDataResponse.Data.EventHome.Layout
                         = EventHomeDataResponse.Data.EventHome.Layout()) : HomeEventItem() {

    var items : MutableList<EventItemModel> = mutableListOf()

    init {
        layout.items.forEach {
            items.add(EventItemModel(
                    it.id,
                    it.rating,
                    it.imageApp,
                    it.title,
                    it.location,
                    it.price,
                    it.schedule,
                    it.isLiked,
                    it.appUrl,
                    it.seoUrl
            ))
        }
    }

    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }
}