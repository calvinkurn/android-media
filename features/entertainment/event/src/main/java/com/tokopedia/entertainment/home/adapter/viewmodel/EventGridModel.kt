package com.tokopedia.entertainment.home.adapter.viewmodel

import com.tokopedia.entertainment.home.adapter.HomeEventItem
import com.tokopedia.entertainment.home.adapter.factory.HomeTypeFactory
import com.tokopedia.entertainment.home.adapter.viewholder.EventGridEventViewHolder
import com.tokopedia.entertainment.home.data.EventHomeDataResponse

/**
 * Author errysuprayogi on 27,January,2020
 */
class EventGridModel(var layout: EventHomeDataResponse.Data.EventHome.Layout)
    : HomeEventItem<HomeTypeFactory> {

    var id : String = ""
    var title : String = ""
    var items : MutableList<EventItemModel> = mutableListOf()

    init {
        id = layout.id
        title = layout.title
        layout.items.forEachIndexed { index, it ->
            if(index < 4) {
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
    }

    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }
}