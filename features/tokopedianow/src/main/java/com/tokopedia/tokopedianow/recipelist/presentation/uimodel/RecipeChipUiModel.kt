package com.tokopedia.tokopedianow.recipelist.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.recipelist.presentation.adapter.RecipeChipTypeFactory

data class RecipeChipUiModel(
    val id: String,
    val title: String,
    val type: ChipType = ChipType.NORMAL
): Visitable<RecipeChipTypeFactory> {

    enum class ChipType {
        NORMAL,
        MORE_FILTER
    }

    override fun type(typeFactory: RecipeChipTypeFactory): Int {
        return typeFactory.type(this)
    }
}