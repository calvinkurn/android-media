package com.tokopedia.tokomart.category.presentation.typefactory

import com.tokopedia.tokomart.category.presentation.model.CategoryIsleDataView
import com.tokopedia.tokomart.searchcategory.presentation.BaseSearchCategoryTypeFactory

interface CategoryTypeFactory: BaseSearchCategoryTypeFactory {

    fun type(categoryIsleDataView: CategoryIsleDataView): Int
}