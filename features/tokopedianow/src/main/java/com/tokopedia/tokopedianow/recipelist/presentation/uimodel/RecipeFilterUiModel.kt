package com.tokopedia.tokopedianow.recipelist.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.recipelist.presentation.adapter.RecipeListTypeFactory

data class RecipeFilterUiModel(
    val selectedFiltersCount: Int = 0
): Visitable<RecipeListTypeFactory> {

    override fun type(typeFactory: RecipeListTypeFactory): Int {
        return typeFactory.type(this)
    }
}