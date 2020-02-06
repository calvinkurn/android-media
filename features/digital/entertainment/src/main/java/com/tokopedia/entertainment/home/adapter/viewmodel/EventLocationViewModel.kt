package com.tokopedia.entertainment.home.adapter.viewmodel

import com.tokopedia.entertainment.home.adapter.HomeEventItem
import com.tokopedia.entertainment.home.adapter.factory.HomeTypeFactory
import com.tokopedia.entertainment.home.adapter.viewholder.EventLocationEventViewHolder

/**
 * Author errysuprayogi on 27,January,2020
 */
class EventLocationViewModel(var titleCard: String,
                             var items: List<EventLocationEventViewHolder.EventItemModel>)
    : HomeEventItem<HomeTypeFactory> {

    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }
}