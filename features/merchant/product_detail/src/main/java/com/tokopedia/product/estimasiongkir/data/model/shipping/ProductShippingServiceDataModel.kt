package com.tokopedia.product.estimasiongkir.data.model.shipping

import com.tokopedia.product.estimasiongkir.view.adapter.ProductShippingFactory
import com.tokopedia.product.estimasiongkir.view.adapter.ProductShippingVisitable

/**
 * Created by Yehezkiel on 26/01/21
 */
data class ProductShippingServiceDataModel(
        val id: Long = 0L,
        val serviceName: String = "", // Regular, Same Day
        val productService: List<ProductServiceDetailDataModel> = listOf()
) : ProductShippingVisitable {
    override fun uniqueId(): Long = id

    override fun isEqual(newData: ProductShippingVisitable): Boolean {
        return newData is ProductShippingHeaderDataModel
    }

    override fun type(typeFactory: ProductShippingFactory): Int {
        return typeFactory.type(this)
    }
}

data class ProductServiceDetailDataModel(
        val serviceProductName: String = "", // JNE,JNT
        val serviceProductEstimation: String = "",
        val serviceProductPrice: String = "",
        val isCod: Boolean = false,
        val codText: String = ""
)