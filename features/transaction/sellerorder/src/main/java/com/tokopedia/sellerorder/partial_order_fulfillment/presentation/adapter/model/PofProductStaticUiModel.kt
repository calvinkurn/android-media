package com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model

import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.PofAdapterTypeFactory

data class PofProductStaticUiModel(
    val orderDetailId: Long,
    val productImageUrl: String,
    val productName: String,
    val productPriceQuantity: String,
    val productQuantity: Int
) : PofVisitable {

    override fun type(typeFactory: PofAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun areItemsTheSame(other: PofVisitable): Boolean {
        return other is PofProductStaticUiModel && orderDetailId == other.orderDetailId
    }

    override fun areContentsTheSame(other: PofVisitable): Boolean {
        return this == other
    }
}
