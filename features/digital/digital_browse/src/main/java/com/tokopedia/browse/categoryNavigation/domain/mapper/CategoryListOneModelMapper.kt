package com.tokopedia.browse.categoryNavigation.domain.mapper

import com.tokopedia.browse.categoryNavigation.data.model.category.CategoryAllList
import com.tokopedia.browse.categoryNavigation.data.model.category.Data


class CategoryListOneModelMapper {

    fun transform(data: Data): CategoryAllList {

        val iterator = data.categoryAllList
        iterator?.categories?.forEach {
            it?.child = null
        }
        return iterator!!

    }
}