package com.tokopedia.entertainment.home.adapter.viewmodel

import com.tokopedia.entertainment.home.adapter.HomeEventItem
import com.tokopedia.entertainment.home.adapter.factory.HomeTypeFactory
import com.tokopedia.entertainment.home.adapter.viewholder.EventLocationEventViewHolder
import com.tokopedia.entertainment.home.data.EventHomeDataResponse

/**
 * Author errysuprayogi on 27,January,2020
 */
class EventLocationViewModel(var loc: EventHomeDataResponse.Data.EventLocationSearch)
    : HomeEventItem<HomeTypeFactory> {

    var items: MutableList<EventLocationEventViewHolder.EventItemModel> = mutableListOf()

    init {
        loc.locations.forEach {
            items.add(EventLocationEventViewHolder.EventItemModel(it.imageApp, it.name, it.address))
        }
    }

    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }
}