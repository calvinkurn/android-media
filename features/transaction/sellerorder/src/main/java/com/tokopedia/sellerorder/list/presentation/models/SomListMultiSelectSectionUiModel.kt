package com.tokopedia.sellerorder.list.presentation.models

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.sellerorder.list.presentation.adapter.typefactories.SomListAdapterTypeFactory

data class SomListMultiSelectSectionUiModel(
    val isEnabled: Boolean,
    val totalOrder: Int,
    val totalSelected: Int,
    val totalSelectable: Int
) : Visitable<SomListAdapterTypeFactory> {
    override fun type(typeFactory: SomListAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}
