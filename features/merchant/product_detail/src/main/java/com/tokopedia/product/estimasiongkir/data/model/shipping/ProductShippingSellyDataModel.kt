package com.tokopedia.product.estimasiongkir.data.model.shipping

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.estimasiongkir.view.adapter.ProductShippingFactory
import com.tokopedia.product.estimasiongkir.view.adapter.ProductShippingVisitable

data class ProductShippingSellyDataModel(
    val services: List<Service> = emptyList(),
    val impressHolder: ImpressHolder = ImpressHolder()
) : ProductShippingVisitable {
    override fun uniqueId(): Long = 0L

    override fun isEqual(newData: ProductShippingVisitable): Boolean = true

    override fun type(typeFactory: ProductShippingFactory): Int = typeFactory.type(this)
}

data class Service(
    val scheduledDate: String = "",
    val isAvailable: Boolean = false,
    val products: List<Product> = emptyList(),
    var isExpanded: Boolean = false
)

data class Product(
    val scheduledTime: String = "",
    val finalPrice: String = "",
    val realPrice: String = "",
    val text: String = "",
    val isRecommend: Boolean = false,
    val isAvailable: Boolean = false
)
