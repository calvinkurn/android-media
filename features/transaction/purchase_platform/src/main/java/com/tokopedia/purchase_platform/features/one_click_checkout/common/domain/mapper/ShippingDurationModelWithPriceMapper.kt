package com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.mapper

import com.tokopedia.purchase_platform.features.one_click_checkout.common.data.model.response.shipping.RangePrice
import com.tokopedia.purchase_platform.features.one_click_checkout.common.data.model.response.shipping.Response
import com.tokopedia.purchase_platform.features.one_click_checkout.common.data.model.response.shipping.ServicesItem
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.shipping.RangePriceModel
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.shipping.ServicesItemModel
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.shipping.ShippingDataModel
import javax.inject.Inject

class ShippingDurationModelWithPriceMapper @Inject constructor(): ShippingDurationDataWithPriceMapper {

    override fun convertToDomainModelWithPrice(response: Response): ShippingDataModel {
        val shippingDataWithPriceModel = ShippingDataModel()
        shippingDataWithPriceModel.id = response.id
        shippingDataWithPriceModel.ratesId = response.ratesId

        val servicesModules = ArrayList<ServicesItemModel?>()
        for (services: ServicesItem in response.services){
            servicesModules.add(getServicesShipping(services))
        }

        shippingDataWithPriceModel.services = servicesModules

        return shippingDataWithPriceModel
    }

    fun getServicesShipping(servicesItem: ServicesItem): ServicesItemModel {
        val servicesItemModel = ServicesItemModel()
        servicesItemModel.serviceId = servicesItem.serviceId
        servicesItemModel.serviceName = servicesItem.serviceName
        servicesItemModel.rangePriceModel = servicesItem.rangePrice?.let { getRangePriceModel(it) }

        return servicesItemModel
    }

    fun getRangePriceModel(rangePrice: RangePrice): RangePriceModel{
        val rangePriceModel = RangePriceModel()
        rangePriceModel.minPrice = rangePrice.minPrice
        rangePriceModel.maxPrice = rangePrice.maxPrice

        return rangePriceModel
    }
}