package com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model

import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.PofAdapterTypeFactory

object PofThickDividerUiModel : PofVisitable {
    override fun type(typeFactory: PofAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun areItemsTheSame(other: PofVisitable): Boolean {
        return other is PofThickDividerUiModel
    }

    override fun areContentsTheSame(other: PofVisitable): Boolean {
        return true
    }
}
