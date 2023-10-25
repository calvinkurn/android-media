package com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model

import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.PofAdapterTypeFactory

data class PofProductEditableUiModel(
    val orderDetailId: Long,
    val productImageUrl: String,
    val productName: String,
    val productPriceQuantity: String,
    val quantityEditorData: QuantityEditorData
) : PofVisitable {

    data class QuantityEditorData(
        val orderDetailId: Long,
        val productId: Long,
        val quantity: Int,
        val maxQuantity: Int,
        val updateTimestamp: Long,
        val enabled: Boolean
    )

    override fun type(typeFactory: PofAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun areItemsTheSame(other: PofVisitable): Boolean {
        return other is PofProductEditableUiModel && orderDetailId == other.orderDetailId
    }

    override fun areContentsTheSame(other: PofVisitable): Boolean {
        return this == other
    }
}
