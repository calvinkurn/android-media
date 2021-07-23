package com.tokopedia.tokopedianow.category.presentation.typefactory

import com.tokopedia.tokopedianow.category.presentation.model.CategoryAisleDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.typefactory.BaseSearchCategoryTypeFactory

interface CategoryTypeFactory: BaseSearchCategoryTypeFactory {

    fun type(categoryAisleDataView: CategoryAisleDataView): Int
}