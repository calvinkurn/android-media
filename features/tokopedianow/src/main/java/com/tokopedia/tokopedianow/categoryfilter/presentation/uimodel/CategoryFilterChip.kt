package com.tokopedia.tokopedianow.categoryfilter.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.categoryfilter.presentation.adapter.CategoryFilterChipAdapter.*
import com.tokopedia.unifycomponents.ChipsUnify

data class CategoryFilterChip(
    val id: String,
    val title: String,
    val chipType: String = ChipsUnify.TYPE_NORMAL,
    val childList: List<CategoryFilterChip> = emptyList()
): Visitable<CategoryFilterChipTypeFactory> {
    override fun type(typeFactory: CategoryFilterChipTypeFactory): Int {
        return typeFactory.type(this)
    }
}