package com.tokopedia.entertainment.home.adapter.viewmodel

import com.tokopedia.entertainment.home.adapter.HomeEventItem
import com.tokopedia.entertainment.home.adapter.factory.HomeTypeFactory
import com.tokopedia.entertainment.home.adapter.viewholder.CategoryEventViewHolder
import com.tokopedia.entertainment.home.data.EventHomeDataResponse

/**
 * Author errysuprayogi on 27,January,2020
 */
class CategoryModel(var category: EventHomeDataResponse.Data.EventChildCategory =
                            EventHomeDataResponse.Data.EventChildCategory()
) : HomeEventItem() {

    var items: MutableList<CategoryEventViewHolder.CategoryItemModel> = mutableListOf()

    init {
        category.categories.removeAt(0)
        category.categories.forEachIndexed { index, category ->
            if (index < 5)
                items.add(CategoryEventViewHolder.CategoryItemModel(category.id,
                        category.mediaUrl, category.title, category.appUrl))
        }
    }

    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }
}