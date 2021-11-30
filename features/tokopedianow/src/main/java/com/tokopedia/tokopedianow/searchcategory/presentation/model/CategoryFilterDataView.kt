package com.tokopedia.tokopedianow.searchcategory.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.searchcategory.presentation.typefactory.BaseSearchCategoryTypeFactory

class CategoryFilterDataView(
        val categoryFilterItemList: List<CategoryFilterItemDataView> = listOf()
): Visitable<BaseSearchCategoryTypeFactory> {

    override fun type(typeFactory: BaseSearchCategoryTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }
}