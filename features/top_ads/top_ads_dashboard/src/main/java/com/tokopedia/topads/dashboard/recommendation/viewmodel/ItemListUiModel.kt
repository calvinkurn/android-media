package com.tokopedia.topads.dashboard.recommendation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.factory.ItemListTypeFactory

data class ItemListUiModel(
    val title: String = "",
    val content: String = "",
    var isSelected: Boolean = false
)
    : Visitable<ItemListTypeFactory> {
    override fun type(typeFactory: ItemListTypeFactory): Int {
        return typeFactory.type(this)
    }
}
