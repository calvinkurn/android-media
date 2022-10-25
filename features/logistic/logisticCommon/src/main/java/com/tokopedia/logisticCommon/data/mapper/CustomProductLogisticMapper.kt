package com.tokopedia.logisticCommon.data.mapper

import com.tokopedia.logisticCommon.data.model.*
import com.tokopedia.logisticCommon.data.response.customproductlogistic.*
import javax.inject.Inject

class CustomProductLogisticMapper @Inject constructor() {

    fun mapCPLData(
        response: GetCPLData,
        draftShipperServices: List<Long>? = null
    ): CustomProductLogisticModel {
        return CustomProductLogisticModel().apply {
            shipperList = mapShipperList(response.shipperList, draftShipperServices)
        }
    }

    fun mapShipperList(
        response: List<ShipperList>,
        draftShipperServices: List<Long>?
    ): List<ShipperListCPLModel> {
        val allShipper = mutableListOf<ShipperListCPLModel>()
        response.forEach {
            val allShipperService = mutableListOf<ShipperCPLModel>()
            allShipperService.addAll(
                mapWhitelabelShipper(
                    it.whitelabelShipper,
                    draftShipperServices
                )
            )
            allShipperService.addAll(mapShipperData(it.shipper, draftShipperServices))

            allShipper.add(
                ShipperListCPLModel(
                    it.header,
                    it.description,
                    allShipperService
                )
            )
        }
        return allShipper
    }

    private fun mapWhitelabelShipper(
        whitelabelShipper: List<WhitelabelShipper>,
        draftShipperServices: List<Long>?
    ): MutableList<ShipperCPLModel> {
        val shipperCPLModel = mutableListOf<ShipperCPLModel>()
        whitelabelShipper.forEach {
            val shipperProducts =
                mapWhitelabelShipperService(it.shipperProductIds, it.isActive, draftShipperServices, it.title)
            val model = ShipperCPLModel(
                shipperName = it.title,
                isWhitelabel = true,
                isActive = shipperProducts.any { sp -> sp.isActive },
                description = it.description,
                shipperProduct = shipperProducts
            )
            shipperCPLModel.add(model)
        }
        return shipperCPLModel
    }

    private fun mapWhitelabelShipperService(
        spIds: List<Long>,
        isWhitelabelServiceActive: Boolean,
        draftShipperServices: List<Long>?,
        serviceName: String
    ): List<ShipperProductCPLModel> {
        return spIds.map { id ->
            val isSpActive = draftShipperServices?.contains(id) ?: isWhitelabelServiceActive
            ShipperProductCPLModel(
                shipperProductId = id,
                isActive = isSpActive,
                shipperServiceName = serviceName
            )
        }
    }

    fun mapShipperData(
        response: List<Shipper>,
        draftShipperServices: List<Long>?
    ): List<ShipperCPLModel> {
        val allShipperData = mutableListOf<ShipperCPLModel>()
        response.forEach {
            val shipperProductData = it.shipperProduct.filter { product -> !product.uiHidden }
            val shipperProducts = mapShipperProduct(shipperProductData, draftShipperServices)
            val isShipperActive = shipperProducts.any { sp -> sp.isActive }
            val description =
                shipperProductData.joinToString(" | ") { shipperProduct -> shipperProduct.shipperProductName }

            val shipperCplModel = ShipperCPLModel(
                shipperId = it.shipperId,
                shipperName = it.shipperName,
                logo = it.logo,
                description = description,
                isActive = isShipperActive,
                shipperProduct = shipperProducts,
            )
            allShipperData.add(shipperCplModel)
        }
        return allShipperData
    }

    fun mapShipperProduct(
        response: List<ShipperProduct>,
        draftShipperServices: List<Long>?
    ): List<ShipperProductCPLModel> {
        return response.filter { !it.uiHidden }.map {
            ShipperProductCPLModel(
                shipperProductId = it.shipperProductId,
                shipperProductName = it.shipperProductName,
                uiHidden = it.uiHidden,
                shipperServiceName = it.shipperServiceName,
                isActive = draftShipperServices?.contains(it.shipperProductId) ?: it.isActive
            )
        }
    }
}
