package com.tokopedia.tokopedianow.category.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.category.presentation.typefactory.CategoryTypeFactory

class CategoryAisleDataView(
        val items: List<CategoryAisleItemDataView>
): Visitable<CategoryTypeFactory> {

    override fun type(typeFactory: CategoryTypeFactory?) =
            typeFactory?.type(this) ?: 0
}