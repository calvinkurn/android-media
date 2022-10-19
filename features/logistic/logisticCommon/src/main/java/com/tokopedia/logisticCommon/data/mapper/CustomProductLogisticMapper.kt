package com.tokopedia.logisticCommon.data.mapper

import com.tokopedia.logisticCommon.data.model.*
import com.tokopedia.logisticCommon.data.response.customproductlogistic.*
import javax.inject.Inject

class CustomProductLogisticMapper @Inject constructor() {

    fun mapCPLData(
        response: GetCPLData,
        productId: String,
        draftShipperServices: List<Long>? = null
    ): CustomProductLogisticModel {
        return CustomProductLogisticModel().apply {
            cplProduct = mapCPLProduct(response.cplProduct, productId, draftShipperServices)
            shipperList = mapShipperList(response.shipperList, draftShipperServices)
        }
    }

    private fun mapCPLProduct(
        response: List<CPLProduct>,
        productId: String,
        draftShipperServices: List<Long>? = null
    ): List<CPLProductModel> {
        return if (response.isNotEmpty()) {
            response.map {
                CPLProductModel(
                    productId = it.productId,
                    cplStatus = draftShipperServices?.getCplStatus() ?: it.cplStatus,
                    shipperServices = draftShipperServices ?: it.shipperServices
                )
            }
        } else {
            draftShipperServices?.mapCPLProductFromDraft(productId) ?: arrayListOf()
        }
    }

    private fun List<Long>.mapCPLProductFromDraft(productId: String): List<CPLProductModel> {
        return arrayListOf(
            CPLProductModel(
                productId = productId.toLong(),
                cplStatus = getCplStatus(),
                shipperServices = this
            )
        )
    }

    private fun List<Long>.getCplStatus(): Int {
        return if (isNotEmpty()) {
            CPL_CUSTOM_SHIPMENT_STATUS
        } else {
            CPL_STANDARD_SHIPMENT_STATUS
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
                mapWhitelabelShipperService(it.shipperProductIds, it.isActive, draftShipperServices)
            val model = ShipperCPLModel(
                shipperName = it.title,
                isWhitelabel = true,
                isActive = shipperProducts.all { sp -> sp.isActive },
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
        draftShipperServices: List<Long>?
    ): List<ShipperProductCPLModel> {
        return spIds.map { id ->
            val isSpActive = draftShipperServices?.contains(id) ?: isWhitelabelServiceActive
            ShipperProductCPLModel(
                shipperProductId = id,
                isActive = isSpActive
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
            val isShipperActive = shipperProducts.all { sp -> sp.isActive }
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
                isActive = draftShipperServices?.contains(it.shipperProductId) ?: it.isActive
            )
        }
    }

    companion object {
        const val CPL_STANDARD_SHIPMENT_STATUS = 0
        const val CPL_CUSTOM_SHIPMENT_STATUS = 1
    }
}
