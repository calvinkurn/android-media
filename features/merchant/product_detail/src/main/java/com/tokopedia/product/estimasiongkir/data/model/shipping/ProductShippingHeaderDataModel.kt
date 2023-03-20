package com.tokopedia.product.estimasiongkir.data.model.shipping

import com.tokopedia.product.detail.common.ProductDetailCommonConstant.BO_TOKONOW
import com.tokopedia.product.detail.common.ProductDetailCommonConstant.BO_TOKONOW_15
import com.tokopedia.product.detail.common.ProductDetailCommonConstant.NO_BEBAS_ONGKIR
import com.tokopedia.product.estimasiongkir.data.model.v3.FreeShipping
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
    val freeOngkirPriceOriginal: Double = 0.0,
    val isFreeOngkirQuotaEmpty: Boolean = false,
    val freeOngkirDesc: String = "",
    val freeOngkirImageUrl: String = "",
    val freeOngkirTokoNowText: String = "",
    val freeOngkirEtas: List<FreeShipping.Eta> = emptyList(),

    val isFulfillment: Boolean = false,
    val tokoCabangIcon: String = "",
    val tokoCabangTitle: String = "",
    val tokoCabangContent: String = "",
    val uspTokoCabangImgUrl: String = "",

    val remoteHideOldBO: Boolean = true
) : ProductShippingVisitable {

    fun shouldShowTxtTokoNow(): Boolean {
        return !remoteHideOldBO && (boType == BO_TOKONOW || boType == BO_TOKONOW_15) && freeOngkirTokoNowText.isNotEmpty()
    }

    fun shouldShowFreeOngkir(): Boolean {
        return if (!remoteHideOldBO && boType == NO_BEBAS_ONGKIR) {
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
