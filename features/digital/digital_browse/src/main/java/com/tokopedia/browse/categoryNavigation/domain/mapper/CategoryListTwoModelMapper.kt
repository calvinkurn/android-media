package com.tokopedia.browse.categoryNavigation.domain.mapper

import com.tokopedia.browse.categoryNavigation.data.model.category.ChildItem
import com.tokopedia.browse.categoryNavigation.data.model.category.Data

class CategoryListTwoModelMapper {

    fun transform(data: Data, id: String): List<ChildItem>? {

        val iterator = data.categoryAllList?.categories
        iterator!!.forEach {
            //it?.child = null
            if (it?.id.equals(id)) {

                var l: MutableList<ChildItem>? = it!!.child as MutableList<ChildItem>?

                if (id.equals("0")) {
                    //it?.type = 1

                    if (l != null) {
                        for (i in l) {
                            i.type = 1
                        }
                    }

                } else {

                    if (l != null) {
                        for (i in l) {
                            i.child?.add(0, ChildItem(null, null, null, null, it.applinks, "Lihat Semua", null, "0", null, null, null, false, 2))
                        }
                    }

                    // val childItem = ChildItem(null, null, null, null, it.applinks, it.name, null, "0", null, null, null, false, 2)
                    // l?.add(0,childItem)
                }

                return l
            }
        }
        return null
    }
}