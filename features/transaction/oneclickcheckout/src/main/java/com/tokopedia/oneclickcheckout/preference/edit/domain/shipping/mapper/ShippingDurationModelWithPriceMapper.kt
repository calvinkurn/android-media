package com.tokopedia.oneclickcheckout.preference.edit.domain.shipping.mapper

import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ServiceData
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ServiceTextData
import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData
import com.tokopedia.oneclickcheckout.preference.edit.view.shipping.model.*
import javax.inject.Inject

class ShippingDurationModelWithPriceMapper @Inject constructor() {

    fun convertToDomainModelWithPrice(response: ShippingRecommendationData): ShippingListModel {
        return ShippingListModel().apply {
            val servicesList: ArrayList<ServicesItem> = ArrayList()
            if (response.shippingDurationViewModels != null) {
                for (item in response.shippingDurationViewModels) {
                    servicesList.add(servicesItemModelMapper(item.serviceData))
                }
            }
            if (response.logisticPromo != null) {
                servicesList.add(LogisticPromoInfo(response.logisticPromo.imageUrl, true))
            }
            services = servicesList
        }
    }

    private fun servicesItemModelMapper(data: ServiceData): ServicesItemModel {
        return ServicesItemModel().apply {
            servicesId = data.serviceId
            servicesName = data.serviceName
            texts = textItemModelMapper(data.texts)
            errorMessage = data.error.errorMessage
            errorId = data.error.errorId
        }
    }

    private fun textItemModelMapper(data: ServiceTextData): TextsModel {
        return TextsModel().apply {
            textRangePrice = data.textRangePrice
            textsServiceDesc = data.textServiceDesc
            textEta = if (data.errorCode == 0) data.textEtaSummarize else null
        }
    }

}