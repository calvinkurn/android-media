package com.tokopedia.entertainment.home.adapter.viewmodel

import com.tokopedia.entertainment.home.adapter.HomeEventItem
import com.tokopedia.entertainment.home.adapter.factory.HomeTypeFactory
import com.tokopedia.entertainment.home.data.EventHomeDataResponse

/**
 * Author errysuprayogi on 27,January,2020
 */
class EventLocationModel(var loc: EventHomeDataResponse.Data.EventLocationSearch =
                EventHomeDataResponse.Data.EventLocationSearch())
    : HomeEventItem() {

    var items: MutableList<EventItemLocationModel> = mutableListOf()

    init {
        loc.locations.forEach {
            items.add(EventItemLocationModel(
                    it.id, it.imageApp,
                    it.name, it.address,
                    it.locationType.name))
        }
    }

    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }
}