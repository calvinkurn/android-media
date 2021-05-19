package com.tokopedia.tokomart.category.presentation.typefactory

import com.tokopedia.tokomart.category.presentation.model.CategoryAisleDataView
import com.tokopedia.tokomart.searchcategory.presentation.typefactory.BaseSearchCategoryTypeFactory

interface CategoryTypeFactory: BaseSearchCategoryTypeFactory {

    fun type(categoryAisleDataView: CategoryAisleDataView): Int
}