package com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model

import android.os.Parcelable
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.PofAdapterTypeFactory
import kotlinx.parcelize.Parcelize

data class PofProductEditableUiModel(
    val orderDetailId: Long,
    val productImageUrl: String,
    val productName: String,
    val productPriceQuantity: String,
    val quantityEditorData: QuantityEditorData
) : PofVisitable {

    @Parcelize
    data class QuantityEditorData(
        val orderDetailId: Long,
        val productId: Long,
        val quantity: Int,
        val minQuantity: Int,
        val maxQuantity: Int,
        val updateTimestamp: Long,
        val enabled: Boolean
    ) : Parcelable

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
