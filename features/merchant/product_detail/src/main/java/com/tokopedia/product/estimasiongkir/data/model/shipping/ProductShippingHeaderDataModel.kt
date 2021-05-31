package com.tokopedia.product.estimasiongkir.data.model.shipping

import com.tokopedia.product.detail.data.util.ProductDetailConstant.NO_BEBAS_ONGKIR
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
        val boType: Int = 0,
        val freeOngkirEstimation: String = "",
        val freeOngkirPrice: String = "",
        val freeOngkirImageUrl: String = "",

        val isFulfillment: Boolean = false,
        val tokoCabangIcon: String = "",
        val tokoCabangTitle: String = "",
        val tokoCabangContent: String = "",
        val uspTokoCabangImgUrl: String = ""
) : ProductShippingVisitable {

    fun shouldShowFreeOngkir(): Boolean {
        return if (boType == NO_BEBAS_ONGKIR) {
            false
        } else {
            freeOngkirImageUrl.isNotEmpty()
        }
    }

    override fun uniqueId(): Long = id

    override fun isEqual(newData: ProductShippingVisitable): Boolean {
        return newData is ProductShippingHeaderDataModel
    }

    override fun type(typeFactory: ProductShippingFactory): Int {
        return typeFactory.type(this)
    }
}