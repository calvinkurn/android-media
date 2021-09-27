package com.tokopedia.logisticCommon.data.mapper

import com.tokopedia.logisticCommon.data.model.*
import com.tokopedia.logisticCommon.data.response.customproductlogistic.*
import javax.inject.Inject

class CustomProductLogisticMapper @Inject constructor() {

    fun mapCPLData(response: GetCPLData): CustomProductLogisticModel {
        return CustomProductLogisticModel().apply {
            cplProduct = mapCPLProduct(response.cplProduct)
            shipperList = mapShipperList(response.shipperList)
        }
    }

    fun mapCPLProduct(response: List<CPLProduct>): List<CPLProductModel> {
        return response.map {
            CPLProductModel(
                it.productId,
                it.cplStatus,
                it.shipperServices
            )
        }
    }

    fun mapShipperList(response: List<ShipperList>): List<ShipperListCPLModel> {
        return response.map {
            ShipperListCPLModel(
                it.header,
                it.description,
                mapShipperData(it.shipper)
            )
        }
    }

    fun mapShipperData(response: List<Shipper>): List<ShipperCPLModel> {
        return response.map {
            ShipperCPLModel(
                it.shipperId,
                it.shipperName,
                it.logo,
                mapShipperProduct(it.shipperProduct)
            )
        }
    }

    fun mapShipperProduct(response: List<ShipperProduct>): List<ShipperProductCPLModel> {
        return response.map {
            ShipperProductCPLModel(
                it.shipperProductId,
                it.shipperProductName,
                it.uiHidden
            )
        }
    }
}