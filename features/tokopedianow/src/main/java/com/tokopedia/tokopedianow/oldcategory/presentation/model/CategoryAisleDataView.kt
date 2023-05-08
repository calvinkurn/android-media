package com.tokopedia.tokopedianow.oldcategory.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.oldcategory.presentation.typefactory.CategoryTypeFactory

class CategoryAisleDataView(
        val items: List<CategoryAisleItemDataView>,
        val serviceType: String
): Visitable<CategoryTypeFactory> {

    override fun type(typeFactory: CategoryTypeFactory?) =
            typeFactory?.type(this) ?: 0
}
