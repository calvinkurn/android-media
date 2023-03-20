package com.tokopedia.product.estimasiongkir.data.model.shipping

import com.tokopedia.product.detail.common.getCurrencyFormatted
import com.tokopedia.product.estimasiongkir.view.adapter.ProductShippingFactory
import com.tokopedia.product.estimasiongkir.view.adapter.ProductShippingVisitable

class FreeShippingDataModel(
    val logoUrl: String = "",
    val title: String = "",
    val description: String = "",
    val estimations: List<Eta> = emptyList(),
    val isQuotaEmpty: Boolean = false
) : ProductShippingVisitable {
    override fun uniqueId() = 0L

    override fun isEqual(newData: ProductShippingVisitable): Boolean {
        return newData is FreeShippingDataModel
    }

    override fun type(typeFactory: ProductShippingFactory): Int {
        return typeFactory.type(this)
    }

    data class Eta(
        val description: String = "",
        val finalPrice: String = "",
        private val rawShippingRate: Double = 0.0,
    ) {
        val originalPrice: String = if (rawShippingRate > 0) rawShippingRate.getCurrencyFormatted() else ""
    }
}
