package com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model

import androidx.annotation.DimenRes
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.PofAdapterTypeFactory

data class PofThinDividerUiModel(
    @DimenRes val marginStart: Int,
    @DimenRes val marginEnd: Int,
    @DimenRes val marginTop: Int,
    @DimenRes val marginBottom: Int
) : PofVisitable {
    override fun type(typeFactory: PofAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun areItemsTheSame(other: PofVisitable): Boolean {
        return other is PofThinDividerUiModel
    }

    override fun areContentsTheSame(other: PofVisitable): Boolean {
        return this == other
    }
}
