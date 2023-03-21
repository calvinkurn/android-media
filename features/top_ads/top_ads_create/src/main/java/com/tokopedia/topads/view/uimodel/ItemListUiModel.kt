package com.tokopedia.topads.view.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.topads.view.adapter.ItemListTypeFactory


data class ItemListUiModel(
    val title: String = "",
    val content: String = "",
//    var isEnabled: Boolean = false,
    var isSelected: Boolean = false
)
    : Visitable<ItemListTypeFactory> {
    override fun type(typeFactory: ItemListTypeFactory): Int {
        return typeFactory.type(this)
    }
}
