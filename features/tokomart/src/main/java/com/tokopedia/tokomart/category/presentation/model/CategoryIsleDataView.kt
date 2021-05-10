package com.tokopedia.tokomart.category.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokomart.category.presentation.typefactory.CategoryTypeFactory

class CategoryIsleDataView(
        val name: String = "",
        val imgUrl: String = ""
): Visitable<CategoryTypeFactory> {

    override fun type(typeFactory: CategoryTypeFactory?) =
            typeFactory?.type(this) ?: 0
}