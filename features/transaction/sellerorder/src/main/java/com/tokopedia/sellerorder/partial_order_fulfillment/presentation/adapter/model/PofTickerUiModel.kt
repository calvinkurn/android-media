package com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model

import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.PofAdapterTypeFactory

data class PofTickerUiModel(
    val text: String
) : PofVisitable {
    override fun type(typeFactory: PofAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun areItemsTheSame(other: PofVisitable): Boolean {
        return other is PofTickerUiModel
    }

    override fun areContentsTheSame(other: PofVisitable): Boolean {
        return this == other
    }
}
