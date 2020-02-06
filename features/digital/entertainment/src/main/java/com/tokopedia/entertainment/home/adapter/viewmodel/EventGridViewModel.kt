package com.tokopedia.entertainment.home.adapter.viewmodel

import com.tokopedia.entertainment.home.adapter.HomeEventItem
import com.tokopedia.entertainment.home.adapter.factory.HomeTypeFactory
import com.tokopedia.entertainment.home.adapter.viewholder.EventCarouselEventViewHolder
import com.tokopedia.entertainment.home.adapter.viewholder.EventGridEventViewHolder
import com.tokopedia.entertainment.home.data.ResponseModel

/**
 * Author errysuprayogi on 27,January,2020
 */
class EventGridViewModel(var layout: ResponseModel.Data.EventHome.Layout)
    : HomeEventItem<HomeTypeFactory> {

    var title : String = ""
    var items : MutableList<EventGridEventViewHolder.EventItemModel> = mutableListOf()

    init {
        title = layout?.title
        layout?.items.forEachIndexed { index, it ->
            if(index < 4) {
                items.add(EventGridEventViewHolder.EventItemModel(
                        it.imageApp,
                        it.title,
                        it.location,
                        it.price,
                        it.schedule
                ))
            }
        }
    }

    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }
}