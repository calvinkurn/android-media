package com.tokopedia.tokopedianow.category.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.tokopedianow.category.presentation.adapter.typefactory.listener.CategoryHeaderSpaceTypeFactory

data class CategoryHeaderSpaceUiModel(
    val id: String = String.EMPTY,
    val space: Int = Int.ZERO,
    val backgroundLightColor: String = String.EMPTY,
    val backgroundDarkColor: String = String.EMPTY
): Visitable<CategoryHeaderSpaceTypeFactory> {
    override fun type(typeFactory: CategoryHeaderSpaceTypeFactory): Int {
        return typeFactory.type(this)
    }
}

