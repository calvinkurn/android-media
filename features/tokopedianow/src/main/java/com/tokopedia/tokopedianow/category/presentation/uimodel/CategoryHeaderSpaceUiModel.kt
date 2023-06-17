package com.tokopedia.tokopedianow.category.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.tokopedianow.category.presentation.adapter.typefactory.listener.CategoryTypeFactory

data class CategoryHeaderSpaceUiModel(
    val id: String = String.EMPTY,
    val space: Int = Int.ZERO,
    val backgroundLightColor: String = String.EMPTY,
    val backgroundDarkColor: String = String.EMPTY
): Visitable<CategoryTypeFactory> {
    override fun type(typeFactory: CategoryTypeFactory): Int = typeFactory.type(this)
}

