package com.tokopedia.browse.categoryNavigation.domain.mapper

import com.tokopedia.browse.categoryNavigation.data.model.hotlist.CategoryHotlist

class CategoryHotlistMapper {
    fun transform(data: CategoryHotlist?, categoryName: String): CategoryHotlist? {

        val item: CategoryHotlist? = data

        item?.list?.forEach {

            it.parentName = categoryName
        }
        return item
    }
}

