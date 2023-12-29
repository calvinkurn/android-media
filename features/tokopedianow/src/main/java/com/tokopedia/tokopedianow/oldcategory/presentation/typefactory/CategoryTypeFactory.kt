package com.tokopedia.tokopedianow.oldcategory.presentation.typefactory

import com.tokopedia.tokopedianow.oldcategory.presentation.model.CategoryAisleDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.typefactory.BaseSearchCategoryTypeFactory

interface CategoryTypeFactory: BaseSearchCategoryTypeFactory {

    fun type(categoryAisleDataView: CategoryAisleDataView): Int
}
