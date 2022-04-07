package com.tokopedia.core.common.category.data.mapper

import com.tokopedia.core.common.category.data.source.db.CategoryDataBase
import com.tokopedia.core.common.category.domain.model.CategoriesResponse
import com.tokopedia.core.common.category.domain.model.Category
import rx.functions.Func1


class CategoryServiceToDbMapper: Func1<CategoriesResponse, List<CategoryDataBase>> {


    override fun call(t: CategoriesResponse): List<CategoryDataBase> {
        val categories = t.categories.categories
        return mapCategories(category = categories,CategoryDataBase.LEVEL_ONE_PARENT)
    }
    private fun mapCategories(category:List<Category>,parent:Int) : List<CategoryDataBase> =
        category.map { mapCategory(it,parent) }

    private fun mapCategory(item:Category,parent:Int): CategoryDataBase =
        CategoryDataBase(
            id = item.id.toLong(),
            name = item.name,
            parentId = parent.toLong(),
            hasChild = item.child.isNotEmpty()
        )
}



