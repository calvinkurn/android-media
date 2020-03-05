package com.tokopedia.browse.categoryNavigation.domain.mapper

import com.tokopedia.browse.categoryNavigation.data.model.category.ChildItem
import com.tokopedia.browse.categoryNavigation.data.model.category.Data

class CategoryListTwoModelMapper {

    enum class CategoryType(val value: Int) {
        SPECIAL(1),
        NORMAL(2)
    }
    val defaultCaseID = "0"

    fun transform(data: Data, id: String): List<ChildItem>? {

        val iterator = data.categoryAllList?.categories
        iterator?.forEach {
            if (it?.id.equals(id)) {

                val levelThreeItem: MutableList<ChildItem>? = it?.child as MutableList<ChildItem>?

                if (id.equals(defaultCaseID)) {

                    if (levelThreeItem != null) {
                        for (item in levelThreeItem) {
                            item.type = CategoryType.SPECIAL.value
                            item.parentCategoryname = it?.name
                        }
                    }
                } else {

                    if (levelThreeItem != null) {
                        for (item in levelThreeItem) {
                            item.parentCategoryname = it?.name
                            item.child?.add(0, ChildItem(item.applinks, item.name ?: "", item.id
                                    ?: "", CategoryType.NORMAL.value))
                        }
                    }

                }

                return levelThreeItem
            }
        }
        return null
    }
}