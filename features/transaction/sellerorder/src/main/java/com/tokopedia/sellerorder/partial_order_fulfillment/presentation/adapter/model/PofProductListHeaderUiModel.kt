package com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model

import com.tokopedia.sellerorder.orderextension.presentation.model.StringRes
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.PofAdapterTypeFactory

data class PofProductListHeaderUiModel(
    val textLeft: StringRes,
    val textRight: StringRes
) : PofVisitable {
    override fun type(typeFactory: PofAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun areItemsTheSame(other: PofVisitable): Boolean {
        return other is PofProductListHeaderUiModel
    }

    override fun areContentsTheSame(other: PofVisitable): Boolean {
        return this == other
    }
}
