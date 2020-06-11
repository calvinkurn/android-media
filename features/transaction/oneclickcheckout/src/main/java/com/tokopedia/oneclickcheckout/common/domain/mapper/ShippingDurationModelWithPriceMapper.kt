package com.tokopedia.oneclickcheckout.common.domain.mapper

import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ServiceData
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ServiceTextData
import com.tokopedia.oneclickcheckout.common.domain.model.shipping.*
import javax.inject.Inject

class ShippingDurationModelWithPriceMapper @Inject constructor() : ShippingDurationDataWithPriceMapper {

    override fun convertToDomainModelWithPrice(response: ShippingRecommendationData): ShippingListModel {
        val servicesList: ArrayList<ServicesItem> = ArrayList()

        return ShippingListModel().apply {
            if (response.shippingDurationViewModels != null) {
                for (item in response.shippingDurationViewModels) {
                    servicesList.add(servicesItemModel(item.serviceData))
                }
            }
            if (response.logisticPromo != null) {
                servicesList.add(0, LogisticPromoInfo(response.logisticPromo.imageUrl))
            }
            services = servicesList
        }
    }

    private val servicesItemModel: (ServiceData) -> ServicesItemModel = {
        ServicesItemModel().apply {
            servicesId = it.serviceId
            servicesName = it.serviceName
            texts = textItemModel(it.texts)
            errorMessage = it.error.errorMessage
            errorId = it.error.errorId
        }
    }

    private val textItemModel: (ServiceTextData) -> TextsModel = {
        TextsModel().apply {
            textRangePrice = it.textRangePrice
            textsServiceDesc = it.textServiceDesc
        }
    }

}