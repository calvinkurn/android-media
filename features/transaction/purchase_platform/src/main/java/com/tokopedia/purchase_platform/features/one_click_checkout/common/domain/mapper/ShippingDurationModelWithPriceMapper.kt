package com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.mapper

import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ServiceData
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ServiceTextData
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.shippingnoprice.ShippingListModel
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.shippingprice.ServicesItemModel
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.shippingprice.TextsModel
import javax.inject.Inject

class ShippingDurationModelWithPriceMapper @Inject constructor() : ShippingDurationDataWithPriceMapper {

    override fun convertToDomainModelWithPrice(response: ShippingRecommendationData): ShippingListModel {
        var servicesList: ArrayList<ServicesItemModel> = ArrayList()

        return ShippingListModel().apply {
            for (item in response.shippingDurationViewModels) {
                servicesList.add(servicesItemModel(item.serviceData))
            }
            this.services = servicesList
        }
    }

    private val servicesItemModel: (ServiceData) -> ServicesItemModel = {
        ServicesItemModel().apply {
            this.servicesId = it.serviceId
            this.servicesName = it.serviceName
            this.texts = textItemModel(it.texts)
        }
    }

    private val textItemModel: (ServiceTextData) -> TextsModel = {
        TextsModel().apply {
            this.textRangePrice = it.textRangePrice
            this.textsServiceDesc = it.textServiceDesc
        }
    }

}