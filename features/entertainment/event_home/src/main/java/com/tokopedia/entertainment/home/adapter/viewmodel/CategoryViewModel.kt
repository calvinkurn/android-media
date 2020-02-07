package com.tokopedia.entertainment.home.adapter.viewmodel

import com.tokopedia.entertainment.home.adapter.HomeEventItem
import com.tokopedia.entertainment.home.adapter.factory.HomeTypeFactory
import com.tokopedia.entertainment.home.adapter.viewholder.CategoryEventViewHolder
import com.tokopedia.entertainment.home.data.EventHomeDataResponse

/**
 * Author errysuprayogi on 27,January,2020
 */
class CategoryViewModel(var category : EventHomeDataResponse.Data.EventChildCategory): HomeEventItem<HomeTypeFactory> {

    var items : MutableList<CategoryEventViewHolder.CategoryItemModel> = mutableListOf()

    init {
        category?.categories.forEach {
            items.add(CategoryEventViewHolder.CategoryItemModel(it.mediaUrl, it.title, it.appUrl))
        }
    }

    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }
}