package com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.mapper

import com.tokopedia.purchase_platform.features.one_click_checkout.common.data.model.response.shipping.Response
import com.tokopedia.purchase_platform.features.one_click_checkout.common.data.model.response.shipping.ServicesItem
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.shipping.ServicesItemModel
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.shipping.ShippingDataModel
import javax.inject.Inject

class ShippingDurationModelMapper @Inject constructor() : ShippingDurationDataMapper{
    override fun convertToDomainModel(response: Response): ShippingDataModel {
        val shippingDataModel = ShippingDataModel()
        shippingDataModel.id = response.id
        shippingDataModel.ratesId = response.ratesId

        val servicesModules = ArrayList<ServicesItemModel?>()
        for (services: ServicesItem in response.services) {
            servicesModules.add(getServicesShipping(services))
        }

        shippingDataModel.services = servicesModules

        return shippingDataModel

    }

    private fun getServicesShipping(servicesItem: ServicesItem): ServicesItemModel {
        val servicesItemModel = ServicesItemModel()
        servicesItemModel.serviceId = servicesItem.serviceId
        servicesItemModel.serviceName = servicesItem.serviceName

        return servicesItemModel
    }
}