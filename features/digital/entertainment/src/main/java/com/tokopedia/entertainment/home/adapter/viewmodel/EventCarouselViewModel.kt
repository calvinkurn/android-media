package com.tokopedia.entertainment.home.adapter.viewmodel

import com.tokopedia.entertainment.home.adapter.HomeItem
import com.tokopedia.entertainment.home.adapter.factory.HomeTypeFactory
import com.tokopedia.entertainment.home.adapter.viewholder.EventCarouselViewHolder

/**
 * Author errysuprayogi on 27,January,2020
 */
class EventCarouselViewModel(var items: List<EventCarouselViewHolder.EventItemModel>) : HomeItem<HomeTypeFactory> {

    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }
}