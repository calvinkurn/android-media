package com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model

import androidx.annotation.ColorRes
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.PofAdapterTypeFactory

data class PofPriceBreakdownUiModel(
    val label: String,
    val value: String,
    @ColorRes val color: Int
) : PofVisitable {
    override fun type(typeFactory: PofAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun areItemsTheSame(other: PofVisitable): Boolean {
        return other is PofPriceBreakdownUiModel && label == other.label
    }

    override fun areContentsTheSame(other: PofVisitable): Boolean {
        return this == other
    }
}
