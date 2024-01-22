package com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model

import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.PofAdapterTypeFactory

data class PofErrorStateUiModel(
    val throwable: Throwable
) : PofVisitable {
    override fun type(typeFactory: PofAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun areItemsTheSame(other: PofVisitable): Boolean {
        return other is PofErrorStateUiModel
    }

    override fun areContentsTheSame(other: PofVisitable): Boolean {
        return this == other
    }
}
