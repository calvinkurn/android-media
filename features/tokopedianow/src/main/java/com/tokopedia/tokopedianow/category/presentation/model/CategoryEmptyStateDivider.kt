package com.tokopedia.tokopedianow.category.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.category.presentation.adapter.typefactory.listener.CategoryL2TypeFactory

data class CategoryEmptyStateDivider(
    val topMargin: Int = DEFAULT_MARGIN,
    val bottomMargin: Int = DEFAULT_MARGIN
): Visitable<CategoryL2TypeFactory> {

    companion object {
        private const val DEFAULT_MARGIN = 0
    }

    override fun type(typeFactory: CategoryL2TypeFactory): Int {
        return typeFactory.type(this)
    }
}
