package com.tokopedia.product.estimasiongkir.data.model.shipping

import com.tokopedia.product.estimasiongkir.view.adapter.ProductShippingFactory
import com.tokopedia.product.estimasiongkir.view.adapter.ProductShippingVisitable

data class ProductShippingSellyDataModel(
    val id: Long = -1,
    val name: String = ""
) : ProductShippingVisitable {
    override fun uniqueId(): Long = id

    override fun isEqual(newData: ProductShippingVisitable): Boolean = true

    override fun type(typeFactory: ProductShippingFactory): Int = typeFactory.type(this)
}
