package com.tokopedia.tokopedianow.recipelist.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.tokopedianow.recipelist.presentation.adapter.RecipeListTypeFactory

data class RecipeEmptyStateUiModel(
    val isFilterSelected: Boolean,
    val title: String
): Visitable<RecipeListTypeFactory>, ImpressHolder() {
    override fun type(typeFactory: RecipeListTypeFactory): Int {
        return typeFactory.type(this)
    }
}