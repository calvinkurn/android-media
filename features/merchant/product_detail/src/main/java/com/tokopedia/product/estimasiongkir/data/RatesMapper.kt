package com.tokopedia.product.estimasiongkir.data

import com.tokopedia.product.estimasiongkir.data.model.RatesEstimateRequest
import com.tokopedia.product.estimasiongkir.data.model.shipping.ProductServiceDetailDataModel
import com.tokopedia.product.estimasiongkir.data.model.shipping.ProductShippingHeaderDataModel
import com.tokopedia.product.estimasiongkir.data.model.shipping.ProductShippingServiceDataModel
import com.tokopedia.product.estimasiongkir.data.model.v3.RatesEstimationModel
import com.tokopedia.product.estimasiongkir.data.model.v3.RatesModel
import com.tokopedia.product.estimasiongkir.view.adapter.ProductShippingVisitable

/**
 * Created by Yehezkiel on 16/02/21
 */
object RatesMapper {
    fun mapToVisitable(ratesModel: RatesEstimationModel, request: RatesEstimateRequest): List<ProductShippingVisitable> {
        val address = ratesModel.address
        val shop = ratesModel.shop
        val weightFormatted = request.getWeightTxt()
        val productShippingHeader = ProductShippingHeaderDataModel(
                id = 1,
                shippingTo = "${address.districtName}, ${address.provinceName}",
                shippingFrom = shop.cityName,
                weight = weightFormatted,
                boType = ratesModel.freeShipping.flag,
                freeOngkirEstimation = ratesModel.freeShipping.etaText,
                freeOngkirImageUrl = request.freeOngkirUrl,
                freeOngkirPrice = ratesModel.freeShipping.shipping_price,
                isFulfillment = request.isFulfillment,
                tokoCabangContent = ratesModel.tokoCabangData.content,
                tokoCabangIcon = ratesModel.tokoCabangData.iconUrl,
                tokoCabangTitle = ratesModel.tokoCabangData.title,
                uspTokoCabangImgUrl = request.uspImageUrl
        )
        val productServiceData: MutableList<ProductShippingVisitable> = mapToServicesData(ratesModel.rates)
        productServiceData.add(0, productShippingHeader)
        return productServiceData
    }

    private fun mapToServicesData(rates: RatesModel): MutableList<ProductShippingVisitable> {
        return rates.services.map { service ->
            val servicesDetail = service.products.map {
                ProductServiceDetailDataModel(it.name, it.eta.textEta, it.price.priceFmt, it.cod.isCodAvailable == 1, it.cod.text)
            }
            ProductShippingServiceDataModel(service.id.toLong(), service.name, servicesDetail)
        }.toMutableList()
    }
}