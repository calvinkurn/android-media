package com.tokopedia.oneclickcheckout.preference.edit.domain.shipping.mapper

import com.tokopedia.oneclickcheckout.preference.edit.data.shipping.Services
import com.tokopedia.oneclickcheckout.preference.edit.data.shipping.ShippingNoPriceResponse
import com.tokopedia.oneclickcheckout.preference.edit.view.shipping.model.ServicesItemModelNoPrice
import com.tokopedia.oneclickcheckout.preference.edit.view.shipping.model.ShippingListModel
import javax.inject.Inject

class ShippingDurationModelMapper @Inject constructor() {

    fun convertToDomainModel(response: ShippingNoPriceResponse): ShippingListModel {
        return ShippingListModel().apply {
            services = response.response.services.map(servicesItemModelMapper)
        }
    }

    private val servicesItemModelMapper: (Services) -> ServicesItemModelNoPrice = {
        ServicesItemModelNoPrice().apply {
            serviceId = it.serviceId
            servicesDuration = it.serviceDuration
        }
    }
}