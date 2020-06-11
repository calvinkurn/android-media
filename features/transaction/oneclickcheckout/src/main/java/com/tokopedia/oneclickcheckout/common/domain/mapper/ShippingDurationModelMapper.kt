package com.tokopedia.oneclickcheckout.common.domain.mapper

import com.tokopedia.oneclickcheckout.common.data.model.response.shipping.Services
import com.tokopedia.oneclickcheckout.common.data.model.response.shipping.ShippingNoPriceResponse
import com.tokopedia.oneclickcheckout.common.domain.model.shipping.ServicesItemModelNoPrice
import com.tokopedia.oneclickcheckout.common.domain.model.shipping.ShippingListModel
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