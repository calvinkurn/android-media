package com.tokopedia.entertainment.home.adapter.viewmodel

import com.tokopedia.entertainment.home.adapter.HomeEventItem
import com.tokopedia.entertainment.home.adapter.factory.HomeTypeFactory
import com.tokopedia.entertainment.home.adapter.viewholder.EventCarouselEventViewHolder
import com.tokopedia.entertainment.home.data.ResponseModel

/**
 * Author errysuprayogi on 27,January,2020
 */
class EventCarouselViewModel(var layout: ResponseModel.Data.EventHome.Layout) : HomeEventItem<HomeTypeFactory> {

    var items : MutableList<EventCarouselEventViewHolder.EventItemModel> = mutableListOf()

    init {
        layout?.items.forEach {
            items.add(EventCarouselEventViewHolder.EventItemModel(
                    it.imageApp,
                    it.title,
                    it.location,
                    it.price,
                    "24 SEP"
            ))
        }
    }

    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }
}