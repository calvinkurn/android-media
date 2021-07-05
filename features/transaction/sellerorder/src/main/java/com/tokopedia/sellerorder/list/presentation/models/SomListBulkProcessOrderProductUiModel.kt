package com.tokopedia.sellerorder.list.presentation.models

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.sellerorder.list.presentation.adapter.typefactories.SomListBulkProcessOrderTypeFactory

data class SomListBulkProcessOrderProductUiModel(
        val productName: String = "",
        val picture: String = "",
        val amount: Int = 0,
        override val showDivider: Boolean = true
): Visitable<SomListBulkProcessOrderTypeFactory>, BaseSomListBulkProcessOrderUiModel {
    override fun type(typeFactory: SomListBulkProcessOrderTypeFactory): Int {
        return typeFactory.type(this)
    }
}