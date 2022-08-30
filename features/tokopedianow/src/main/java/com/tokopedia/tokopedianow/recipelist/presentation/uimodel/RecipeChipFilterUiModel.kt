package com.tokopedia.tokopedianow.recipelist.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.recipelist.presentation.adapter.RecipeChipFilterTypeFactory

data class RecipeChipFilterUiModel(
    val id: String,
    val title: String,
    val type: ChipType = ChipType.NORMAL
): Visitable<RecipeChipFilterTypeFactory> {

    enum class ChipType {
        NORMAL,
        MORE_FILTER
    }

    override fun type(typeFactory: RecipeChipFilterTypeFactory): Int {
        return typeFactory.type(this)
    }
}