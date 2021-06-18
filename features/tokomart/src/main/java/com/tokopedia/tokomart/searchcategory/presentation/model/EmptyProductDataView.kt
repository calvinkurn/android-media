package com.tokopedia.tokomart.searchcategory.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.filter.common.data.Option
import com.tokopedia.tokomart.searchcategory.presentation.typefactory.BaseSearchCategoryTypeFactory

class EmptyProductDataView(val activeFilterList: List<Option>): Visitable<BaseSearchCategoryTypeFactory> {

    override fun type(typeFactory: BaseSearchCategoryTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }
}