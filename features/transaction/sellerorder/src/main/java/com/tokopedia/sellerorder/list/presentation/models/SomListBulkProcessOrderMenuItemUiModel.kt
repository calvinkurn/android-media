package com.tokopedia.sellerorder.list.presentation.models

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.sellerorder.list.presentation.adapter.typefactories.SomListBulkProcessOrderTypeFactory

data class SomListBulkProcessOrderMenuItemUiModel(
        val keyAction: String = "",
        val text: String = "",
        val enable: Boolean = true,
        override val showDivider: Boolean = true
): Visitable<SomListBulkProcessOrderTypeFactory>, BaseSomListBulkProcessOrderUiModel {
    override fun type(typeFactory: SomListBulkProcessOrderTypeFactory): Int {
        return typeFactory.type(this)
    }
}