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
            shipperList = mapShipperList(response.shipperList)
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

    fun mapShipperList(response: List<ShipperList>): List<ShipperListCPLModel> {
        val allShipper = mutableListOf<ShipperListCPLModel>()
        response.forEach {
            val allShipperService = mutableListOf<ShipperCPLModel>()
            allShipperService.addAll(mapWhitelabelShipper(it.whitelabelShipper))
            allShipperService.addAll(mapShipperData(it.shipper))
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

    private fun mapWhitelabelShipper(whitelabelShipper: List<WhitelabelShipper>): MutableList<ShipperCPLModel> {
        val shipperCPLModel = mutableListOf<ShipperCPLModel>()
        whitelabelShipper.forEach {
            val model = ShipperCPLModel(
                shipperName = it.title,
                isWhitelabel = true,
                isActive = it.isActive,
                description = it.description,
                shipperProduct = it.shipperProductIds.map { id ->
                    ShipperProductCPLModel(
                        shipperProductId = id,
                        isActive = it.isActive
                    )
                }
            )
            shipperCPLModel.add(model)
        }
        return shipperCPLModel
    }

    fun mapShipperData(response: List<Shipper>): List<ShipperCPLModel> {
        val allShipperData = mutableListOf<ShipperCPLModel>()
        response.forEach {
            val shipperProductData = it.shipperProduct.filter { product -> product.uiHidden }
            val description =
                shipperProductData.joinToString(" | ") { shipperProduct -> shipperProduct.shipperProductName }
            val shipperCplModel = ShipperCPLModel(
                shipperId = it.shipperId,
                shipperName = it.shipperName,
                logo = it.logo,
                description = description,
                shipperProduct = mapShipperProduct(shipperProductData),
            )
            allShipperData.add(shipperCplModel)
        }
        return allShipperData
    }

    fun mapShipperProduct(response: List<ShipperProduct>): List<ShipperProductCPLModel> {
        return response.map {
            ShipperProductCPLModel(
                it.shipperProductId,
                it.shipperProductName,
                it.uiHidden,
            )
        }
    }

    companion object {
        const val CPL_STANDARD_SHIPMENT_STATUS = 0
        const val CPL_CUSTOM_SHIPMENT_STATUS = 1
    }
}
