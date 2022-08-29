package com.tokopedia.tokopedianow.search.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.search.presentation.typefactory.SearchTypeFactory

data class CategoryJumperDataView(
        val title: String = "",
        val itemList: List<Item> = listOf(),
        val serviceType: String = ""
): Visitable<SearchTypeFactory> {

    override fun type(typeFactory: SearchTypeFactory?) =
            typeFactory?.type(this) ?: 0

    data class Item(
            val title: String = "",
            val applink: String = "",
    )
}