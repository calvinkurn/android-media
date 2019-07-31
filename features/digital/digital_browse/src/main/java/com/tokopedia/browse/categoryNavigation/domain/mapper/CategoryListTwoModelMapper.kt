package com.tokopedia.browse.categoryNavigation.domain.mapper

import com.tokopedia.browse.categoryNavigation.data.model.category.ChildItem
import com.tokopedia.browse.categoryNavigation.data.model.category.Data

class CategoryListTwoModelMapper {

    fun transform(data: Data, id: String): List<ChildItem>? {

        val iterator = data.categoryAllList?.categories
        iterator!!.forEach {
            if (it?.id.equals(id)) {

                val l: MutableList<ChildItem>? = it!!.child as MutableList<ChildItem>?

                if (id.equals("0")) {

                    if (l != null) {
                        for (i in l) {
                            i.type = 1
                            i.parentCategoryname = it.name
                        }
                    }

                } else {

                    if (l != null) {
                        for (i in l) {
                            i.parentCategoryname = it.name
                            i.child?.add(0, ChildItem(i.applinks, "Lihat Semua", "0", 2))
                        }
                    }

                }

                return l
            }
        }
        return null
    }
}