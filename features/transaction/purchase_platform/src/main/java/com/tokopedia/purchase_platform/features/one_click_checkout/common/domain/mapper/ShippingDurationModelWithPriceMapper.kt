package com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.mapper

import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.RatesGqlResponse
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ServiceData
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ServiceTextData
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.shippingnoprice.ShippingListModel
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.shippingprice.ServicesItemModel
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.shippingprice.ShippingDataModel
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.shippingprice.TextsModel
import javax.inject.Inject

class ShippingDurationModelWithPriceMapper @Inject constructor(): ShippingDurationDataWithPriceMapper {

    override fun convertToDomainModelWithPrice(response: ShippingRecommendationData): ShippingListModel {
        var servicesList: ArrayList<ServicesItemModel> = ArrayList()

        return ShippingListModel().apply {
            for (item in response.shippingDurationViewModels) {
                servicesList.add(servicesItemModel(item.serviceData))
            }
            this.services = servicesList
        }
    }

/*
    override fun convertToDomainModelWithPrice(response: ShippingRecommendationData): ShippingListModel {

        return ShippingListModel().apply {
            this.servicesPrice = response.shippingDurationViewModels.map {
//                it.serviceData
            }
//            this.servicesPrice = response.ratesData.ratesDetailData.services.map(servicesItemModel)
        }
    }*/


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

/*    //response dari getRates
    override fun convertToDomainModelWithPrice(response: RatesGqlResponse): ShippingDataModel {
        val shippingDataWithPriceModel = ShippingDataModel()
        shippingDataWithPriceModel.id = response.id
        shippingDataWithPriceModel.ratesId = response.ratesId

        val servicesModules = ArrayList<ServicesItemModel?>()
        for (services: ServicesItem in response.services){
            servicesModules.add(getServicesShipping(services))
        }

        shippingDataWithPriceModel.services = servicesModules

        return shippingDataWithPriceModel
    }*/
/*
    fun getServicesShipping(servicesItem: ServicesItem): ServicesItemModel {
        val servicesItemModel = ServicesItemModel()
        servicesItemModel.serviceId = servicesItem.serviceId
        servicesItemModel.serviceName = servicesItem.serviceName
        servicesItemModel.rangePriceModel = servicesItem.rangePrice?.let { getRangePriceModel(it) }

        return servicesItemModel
    }

    fun getRangePriceModel(rangePrice: RangePrice): RangePriceModel{
        val rangePriceModel = RangePriceModel()
        rangePriceModel.rangePrice = (rangePrice.minPrice.toString() + "-" + rangePrice.maxPrice.toString())

        return rangePriceModel
    }*/
}