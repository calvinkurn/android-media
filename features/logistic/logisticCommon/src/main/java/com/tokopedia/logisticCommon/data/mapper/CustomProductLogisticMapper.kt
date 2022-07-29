package com.tokopedia.logisticCommon.data.mapper

import com.tokopedia.logisticCommon.data.model.*
import com.tokopedia.logisticCommon.data.response.customproductlogistic.*
import javax.inject.Inject

class CustomProductLogisticMapper @Inject constructor() {

    fun mapCPLData(response: GetCPLData, draftShipperServices: List<Long>? = null): CustomProductLogisticModel {
        return CustomProductLogisticModel().apply {
            cplProduct = mapCPLProduct(response.cplProduct, draftShipperServices)
            shipperList = mapShipperList(response.shipperList)
        }
    }

    private fun mapCPLProduct(response: List<CPLProduct>, draftShipperServices: List<Long>? = null): List<CPLProductModel> {
        return response.map {
            CPLProductModel(
                productId = it.productId,
                cplStatus = draftShipperServices?.getCplStatus() ?: it.cplStatus,
                shipperServices = draftShipperServices ?: it.shipperServices
            )
        }
    }

    private fun List<Long>.getCplStatus(): Int {
        return if (isNotEmpty()) {
            CPL_CUSTOM_SHIPMENT_STATUS
        } else {
            CPL_STANDARD_SHIPMENT_STATUS
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

    companion object {
        const val CPL_STANDARD_SHIPMENT_STATUS = 0
        const val CPL_CUSTOM_SHIPMENT_STATUS = 1
    }
}