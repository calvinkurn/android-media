package com.tokopedia.editshipping.domain.mapper

import com.tokopedia.editshipping.domain.model.shippingEditor.*
import com.tokopedia.logisticCommon.data.response.shippingeditor.*
import javax.inject.Inject

class ShipperDetailMapper @Inject constructor() {

    fun mapShipperDetails(response: DataShipperDetail): ShipperDetailModel {
        return ShipperDetailModel().apply {
            shipperDetails = mapShipperDetails(response.shipperDetails)
            featureDetails = mapFeatureDetail(response.featureDetails)
            serviceDetails = mapServiceDetail(response.serviceDetails)
        }
    }

    private fun mapShipperDetails(data: List<ShipperDetails>): List<ShipperDetailsModel> {
        val shipperDetailsList = ArrayList<ShipperDetailsModel>()
        data.forEach { data ->
            val shipperDetailsUiModel = ShipperDetailsModel().apply {
                name = data.name
                description = data.description
                image = data.image
                shipperProduct = mapShipperProduct(data.shipperProduct)
            }
            shipperDetailsList.add(shipperDetailsUiModel)
        }
        return shipperDetailsList
    }

    private fun mapShipperProduct(data: List<ShipperProductDetails>): List<ShipperProductDetailsModel> {
        val shipperProductList = ArrayList<ShipperProductDetailsModel>()
        data.forEach { data ->
            val shopperProductUiModel = ShipperProductDetailsModel().apply {
                name = data.name
                description = data.description
            }
            shipperProductList.add(shopperProductUiModel)
        }
        return shipperProductList
    }

    private fun mapFeatureDetail(data: List<FeatureDetails>): List<FeatureDetailsModel> {
        val featureDetailsList = ArrayList<FeatureDetailsModel>()
        data.forEach { data ->
            val featureDetailsUiModel = FeatureDetailsModel().apply {
                 header = data.header
                description = data.description
            }
            featureDetailsList.add(featureDetailsUiModel)
        }
        return featureDetailsList
    }

    private fun mapServiceDetail(data: List<ServiceDetails>): List<ServiceDetailsModel> {
        val serviceDetailsList = ArrayList<ServiceDetailsModel>()
        data.forEach { data ->
            val serviceDetailsUiModel = ServiceDetailsModel().apply {
                header = data.header
                description = data.description
            }
            serviceDetailsList.add(serviceDetailsUiModel)
        }
        return serviceDetailsList
    }

}