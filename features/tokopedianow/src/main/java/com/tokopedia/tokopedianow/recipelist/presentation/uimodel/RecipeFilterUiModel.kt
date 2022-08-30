package com.tokopedia.tokopedianow.recipelist.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.recipelist.presentation.adapter.RecipeListTypeFactory

data class RecipeFilterUiModel(
    val chips: List<RecipeChipFilterUiModel>
): Visitable<RecipeListTypeFactory> {

    override fun type(typeFactory: RecipeListTypeFactory): Int {
        return typeFactory.type(this)
    }
}