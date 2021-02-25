package com.tokopedia.product.estimasiongkir.data.model.shipping

import com.tokopedia.product.estimasiongkir.view.adapter.ProductShippingFactory
import com.tokopedia.product.estimasiongkir.view.adapter.ProductShippingVisitable

/**
 * Created by Yehezkiel on 25/01/21
 */
data class ProductShippingHeaderDataModel(
        val id: Long = 0L,
        val shippingTo: String = "",
        val shippingFrom: String = "",
        val weight: String = "",
        val isFreeOngkir: Boolean = false,
        val freeOngkirEstimation: String = "",
        val freeOngkirPrice: String = "",
        val freeOngkirImageUrl: String = "",

        val isFulfillment: Boolean = false,
        val tokoCabangIcon: String = "",
        val tokoCabangTitle: String = "",
        val tokoCabangContent: String = ""
) : ProductShippingVisitable {
    override fun uniqueId(): Long = id

    override fun isEqual(newData: ProductShippingVisitable): Boolean {
        return newData is ProductShippingHeaderDataModel
    }

    override fun type(typeFactory: ProductShippingFactory): Int {
        return typeFactory.type(this)
    }
}