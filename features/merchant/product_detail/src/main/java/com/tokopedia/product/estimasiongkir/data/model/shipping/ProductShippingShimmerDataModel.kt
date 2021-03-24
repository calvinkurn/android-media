package com.tokopedia.product.estimasiongkir.data.model.shipping

import com.tokopedia.product.estimasiongkir.view.adapter.ProductShippingFactory
import com.tokopedia.product.estimasiongkir.view.adapter.ProductShippingVisitable

/**
 * Created by Yehezkiel on 08/02/21
 */
class ProductShippingShimmerDataModel(
        val id: Long = 0L,
        val height: Int = 0
) : ProductShippingVisitable {

    companion object {
        const val SHIMMER_UNIQUE_ID = 919191L
    }

    override fun uniqueId(): Long {
        return SHIMMER_UNIQUE_ID
    }

    override fun isEqual(newData: ProductShippingVisitable): Boolean {
        return newData is ProductShippingShimmerDataModel
    }

    override fun type(typeFactory: ProductShippingFactory): Int {
        return typeFactory.type(this)
    }
}