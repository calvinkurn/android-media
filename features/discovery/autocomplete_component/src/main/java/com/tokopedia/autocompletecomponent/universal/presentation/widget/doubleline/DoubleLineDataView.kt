package com.tokopedia.autocompletecomponent.universal.presentation.widget.doubleline

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocompletecomponent.universal.presentation.model.RelatedItemDataView
import com.tokopedia.autocompletecomponent.universal.presentation.typefactory.UniversalSearchTypeFactory

class DoubleLineDataView(
    val id: String = "",
    val applink: String = "",
    val title: String = "",
    val subtitle: String = "",
    val componentId: String = "",
    val trackingOption: Int = 0,
    val related: List<RelatedItemDataView> = listOf()
): Visitable<UniversalSearchTypeFactory> {

    override fun type(typeFactory: UniversalSearchTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }
}