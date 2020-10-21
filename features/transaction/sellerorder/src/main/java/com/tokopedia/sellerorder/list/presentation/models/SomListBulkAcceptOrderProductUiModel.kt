package com.tokopedia.sellerorder.list.presentation.models

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.sellerorder.list.presentation.adapter.typefactories.SomListBulkAcceptOrderTypeFactory

data class SomListBulkAcceptOrderProductUiModel(
        val productName: String = "",
        val picture: String = "",
        val amount: Int = 0
): Visitable<SomListBulkAcceptOrderTypeFactory> {
    override fun type(typeFactory: SomListBulkAcceptOrderTypeFactory?): Int {
        return typeFactory?.type(this).orZero()
    }
}