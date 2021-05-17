package com.tokopedia.tokomart.searchcategory.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.tokomart.searchcategory.presentation.typefactory.BaseSearchCategoryTypeFactory

class QuickFilterDataView(
        val quickFilterItemList: List<SortFilterItemDataView> = listOf()
): Visitable<BaseSearchCategoryTypeFactory> {

    override fun type(typeFactory: BaseSearchCategoryTypeFactory?) =
            typeFactory?.type(this) ?: 0
}