package com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model

import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.PofAdapterTypeFactory

data class PofPriceBreakdownTotalUiModel(
    val label: String,
    val value: String
) : PofVisitable {
    override fun type(typeFactory: PofAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun areItemsTheSame(other: PofVisitable): Boolean {
        return other is PofPriceBreakdownTotalUiModel && label == other.label
    }

    override fun areContentsTheSame(other: PofVisitable): Boolean {
        return this == other
    }
}
