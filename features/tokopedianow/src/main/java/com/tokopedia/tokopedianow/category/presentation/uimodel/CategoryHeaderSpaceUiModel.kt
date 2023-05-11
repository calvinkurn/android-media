package com.tokopedia.tokopedianow.category.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.category.presentation.adapter.CategoryTypeFactory

class CategoryHeaderSpaceUiModel(
    val space: Int,
    val backgroundColor: String
): Visitable<CategoryTypeFactory> {
    override fun type(typeFactory: CategoryTypeFactory): Int = typeFactory.type(this)
}

