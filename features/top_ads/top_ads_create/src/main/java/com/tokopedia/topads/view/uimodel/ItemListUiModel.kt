package com.tokopedia.topads.view.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.topads.view.adapter.ListBottomSheetItemFactory


data class ItemListUiModel(
    val title: String = "",
    val content: String = "",
    var isSelected: Boolean = false
)
    : Visitable<ListBottomSheetItemFactory> {
    override fun type(typeFactory: ListBottomSheetItemFactory): Int {
        return typeFactory.type(this)
    }
}
