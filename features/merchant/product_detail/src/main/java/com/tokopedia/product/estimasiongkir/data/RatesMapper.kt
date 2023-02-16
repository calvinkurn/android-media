package com.tokopedia.product.estimasiongkir.data

import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.product.estimasiongkir.data.model.RatesEstimateRequest
import com.tokopedia.product.estimasiongkir.data.model.ScheduledDeliveryRatesModel
import com.tokopedia.product.estimasiongkir.data.model.shipping.Product
import com.tokopedia.product.estimasiongkir.data.model.shipping.ProductServiceDetailDataModel
import com.tokopedia.product.estimasiongkir.data.model.shipping.ProductShippingHeaderDataModel
import com.tokopedia.product.estimasiongkir.data.model.shipping.ProductShippingSellyDataModel
import com.tokopedia.product.estimasiongkir.data.model.shipping.ProductShippingServiceDataModel
import com.tokopedia.product.estimasiongkir.data.model.shipping.WhiteLabelDataModel
import com.tokopedia.product.estimasiongkir.data.model.shipping.Service
import com.tokopedia.product.estimasiongkir.data.model.v3.RatesEstimationModel
import com.tokopedia.product.estimasiongkir.data.model.v3.RatesModel
import com.tokopedia.product.estimasiongkir.data.model.v3.ServiceBasedShipment
import com.tokopedia.product.estimasiongkir.data.model.v3.ServiceModel
import com.tokopedia.product.estimasiongkir.view.adapter.ProductShippingVisitable

/**
 * Created by Yehezkiel on 16/02/21
 */
object RatesMapper {
    private const val COD_AVAILABLE = 1

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
                freeOngkirPrice = ratesModel.freeShipping.shippingPrice,
                freeOngkirPriceOriginal = ratesModel.freeShipping.rawShippingRate,
                isFreeOngkirQuotaEmpty = ratesModel.freeShipping.isQuotaEmpty,
                freeOngkirDesc = ratesModel.freeShipping.desc,
                isFulfillment = request.isFulfillment,
                tokoCabangContent = ratesModel.tokoCabangData.content,
                tokoCabangIcon = ratesModel.tokoCabangData.iconUrl,
                tokoCabangTitle = ratesModel.tokoCabangData.title,
                uspTokoCabangImgUrl = request.uspImageUrl,
                freeOngkirTokoNowText = ratesModel.freeShipping.title,
                freeOngkirEtas = ratesModel.freeShipping.freeShippingEtas
        )
        val productServiceData: MutableList<ProductShippingVisitable> = mapToServicesData(ratesModel.rates)
        productServiceData.add(0, productShippingHeader)
        return productServiceData
    }

    fun mapToVisitable(scheduledDeliveryRatesModel: ScheduledDeliveryRatesModel?): List<ProductShippingVisitable> {
        if (scheduledDeliveryRatesModel == null) return emptyList()
        val services =
            scheduledDeliveryRatesModel.deliveryServices.filter { !it.isHidden }.map { service ->
                val products = service.deliveryProducts.filter { !it.isHidden }.map { product ->
                    Product(
                        scheduledTime = product.title,
                        finalPrice = product.textFinalPrice,
                        realPrice = product.textRealPrice,
                        isAvailable = product.isAvailable,
                        isRecommend = product.isRecommend,
                        text = product.text
                    )
                }

                val scheduleDate = service.titleLabel.let {
                    if (it.isNotEmpty()) ", $it"
                    else ""
                }
                Service(
                    scheduledDate = service.title + scheduleDate,
                    products = products,
                    isAvailable = service.isAvailable
                )
            }
        return if (services.isEmpty()) emptyList()
        else listOf(ProductShippingSellyDataModel(services = services))
    }

    private fun mapToServicesData(rates: RatesModel): MutableList<ProductShippingVisitable> {
        return rates.services.mapNotNull { service ->
            val serviceBasedShipment = service.serviceBasedShipment
            val isWhitelabel = serviceBasedShipment.isAvailable

            if (isWhitelabel) mapWhiteLabel(service, serviceBasedShipment)
            else mapGeneralServiceProducts(service)
        }.toMutableList()
    }

    private fun mapWhiteLabel(
        service: ServiceModel,
        whiteLabelData: ServiceBasedShipment
    ): ProductShippingServiceDataModel {
        return ProductShippingServiceDataModel(
            id = service.id.toLongOrZero(),
            serviceName = service.name,
            whiteLabelData = WhiteLabelDataModel(
                eta = whiteLabelData.textEta,
                price = whiteLabelData.textPrice
            )
        )
    }

    private fun mapGeneralServiceProducts(service: ServiceModel): ProductShippingServiceDataModel? {
        val servicesDetail = service.products.filter { !it.uiRatesHidden }.map {
            ProductServiceDetailDataModel(
                it.name,
                it.eta.textEta,
                it.price.priceFmt,
                it.cod.isCodAvailable == COD_AVAILABLE,
                it.cod.text,
                it.features.dynamicPrice.dynamicPriceString
            )
        }
        return if (servicesDetail.isNotEmpty()) {
            ProductShippingServiceDataModel(service.id.toLongOrZero(), service.name, servicesDetail)
        } else null
    }
}
