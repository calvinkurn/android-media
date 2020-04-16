package com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.mapper

import com.tokopedia.purchase_platform.features.one_click_checkout.common.data.model.response.Services
import com.tokopedia.purchase_platform.features.one_click_checkout.common.data.model.response.ShippingNoPriceResponse
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.shippingnoprice.ShippingListModel
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.shippingprice.ServicesItemModelNoPrice
import javax.inject.Inject

class ShippingDurationModelMapper @Inject constructor() : ShippingDurationDataMapper{

    override fun convertToDomainModel(response: ShippingNoPriceResponse): ShippingListModel {

        return ShippingListModel().apply {
            this.services = response.response.services.map(servicesItemModel)
        }

    }

    private val servicesItemModel: (Services) -> ServicesItemModelNoPrice = {
        ServicesItemModelNoPrice().apply {
            this.serviceId = it.serviceId
            this.servicesDuration = it.serviceDuration
            this.serviceCode = it.serviceCode
        }
    }
}