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
        return data.map {
            ShipperDetailsModel(
                    it.name,
                    it.description,
                    it.image,
                    mapShipperProduct(it.shipperProduct)
            )
        }
    }

    private fun mapShipperProduct(data: List<ShipperProductDetails>): List<ShipperProductDetailsModel> {
        return data.map {
            ShipperProductDetailsModel(
                    it.name,
                    it.description
            )
        }
    }

    private fun mapFeatureDetail(data: List<FeatureDetails>): List<FeatureDetailsModel> {
        return data.map {
            FeatureDetailsModel(
                    it.header,
                    it.description
            )
        }
    }

    private fun mapServiceDetail(data: List<ServiceDetails>): List<ServiceDetailsModel> {
        return data.map {
            ServiceDetailsModel(
                    it.header,
                    it.description
            )
        }
    }

}