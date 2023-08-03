package com.tokopedia.logisticcart.utils

import com.google.gson.Gson
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticcart.shipping.model.Product
import com.tokopedia.logisticcart.shipping.model.RatesParam
import com.tokopedia.logisticcart.shipping.model.ShipmentDetailData
import com.tokopedia.logisticcart.shipping.model.ShippingParam
import com.tokopedia.logisticcart.shipping.model.ShopShipment
import com.tokopedia.purchase_platform.common.feature.bometadata.BoMetadata

internal object RatesParamHelper {

    private const val WEIGHT_DIVIDER_TO_KG = 1000

    fun generateSpIds(input: List<ShopShipment>): String {
        val result: MutableList<Int> = mutableListOf()
        input.forEach { shop ->
            shop.shipProds.forEach { ship ->
                result.add(ship.shipProdId)
            }
        }
        return result.joinToString(",")
    }

    fun generateOrigin(input: ShippingParam): String {
        val result: MutableList<String> = mutableListOf()
        result.add(input.originDistrictId ?: "")
        if (!input.originPostalCode.isNullOrEmpty()) {
            result.add(input.originPostalCode ?: "0")
        } else {
            result.add("0")
        }

        val latlng = "${input.originLatitude ?: ""},${input.originLongitude ?: ""}"
        result.add(latlng)
        return result.joinToString(separator = "|")
    }

    fun generateDestination(input: ShippingParam): String {
        val result: MutableList<String> = mutableListOf()
        result.add(input.destinationDistrictId ?: "")
        if (!input.destinationPostalCode.isNullOrEmpty()) {
            result.add(input.destinationPostalCode ?: "0")
        } else {
            result.add("0")
        }

        val latlng = "${input.destinationLatitude ?: ""},${input.destinationLongitude ?: ""}"
        result.add(latlng)
        return result.joinToString(separator = "|")
    }

    fun determineTradeIn(shipping: ShippingParam): Int = when {
        shipping.isTradeInDropOff -> 2
        shipping.isTradein -> 1
        else -> 0
    }

    fun generateProducts(shipping: ShippingParam): String {
        val gson = Gson()
        val json = gson.toJson(shipping.products)
        return json.replace("\n", "")
    }

    fun generateBoMetadata(boMetadata: BoMetadata?): String {
        if (boMetadata != null) {
            val gson = Gson()
            val json = gson.toJson(mapOf("bo_metadata" to boMetadata))
            return json.replace("\n", "")
        }
        return ""
    }

    fun generateRatesParam(
        shipmentDetailData: ShipmentDetailData,
        shopShipmentList: List<ShopShipment>,
        recipientAddressModel: RecipientAddressModel? = null,
        codHistory: Int = -1,
        isLeasing: Boolean = false,
        pslCode: String = "",
        products: ArrayList<Product>,
        cartString: String,
        isTradeInDropOff: Boolean = false,
        mvc: String = "",
        cartData: String,
        isOcc: Boolean,
        warehouseId: String
    ): RatesParam {
        val shippingParam = generateShippingParam(
            shipmentDetailData,
            products,
            cartString,
            isTradeInDropOff,
            recipientAddressModel
        )
        return RatesParam.Builder(shopShipmentList, shippingParam)
            .isCorner(recipientAddressModel?.isCornerAddress ?: false)
            .codHistory(codHistory)
            .isLeasing(isLeasing)
            .promoCode(pslCode)
            .mvc(mvc)
            .isOcc(isOcc)
            .cartData(cartData)
            .warehouseId(warehouseId)
            .build()
    }

    private fun generateShippingParam(
        shipmentDetailData: ShipmentDetailData,
        products: List<Product>,
        cartString: String,
        isTradeInDropOff: Boolean,
        recipientAddressModel: RecipientAddressModel?
    ): ShippingParam {
        val shippingParam = ShippingParam()
        shippingParam.originDistrictId = shipmentDetailData.shipmentCartData!!.originDistrictId
        shippingParam.originPostalCode = shipmentDetailData.shipmentCartData!!.originPostalCode
        shippingParam.originLatitude = shipmentDetailData.shipmentCartData!!.originLatitude
        shippingParam.originLongitude = shipmentDetailData.shipmentCartData!!.originLongitude
        shippingParam.weightInKilograms =
            shipmentDetailData.shipmentCartData!!.weight / WEIGHT_DIVIDER_TO_KG
        shippingParam.weightActualInKilograms =
            shipmentDetailData.shipmentCartData!!.weightActual / WEIGHT_DIVIDER_TO_KG
        shippingParam.shopId = shipmentDetailData.shopId
        shippingParam.shopTier = shipmentDetailData.shipmentCartData!!.shopTier
        shippingParam.token = shipmentDetailData.shipmentCartData!!.token
        shippingParam.ut = shipmentDetailData.shipmentCartData!!.ut
        shippingParam.insurance = shipmentDetailData.shipmentCartData!!.insurance
        shippingParam.productInsurance = shipmentDetailData.shipmentCartData!!.productInsurance
        shippingParam.orderValue = shipmentDetailData.shipmentCartData!!.orderValue
        shippingParam.categoryIds = shipmentDetailData.shipmentCartData!!.categoryIds
        shippingParam.isBlackbox = shipmentDetailData.isBlackbox
        shippingParam.isPreorder = shipmentDetailData.preorder
        shippingParam.addressId = shipmentDetailData.addressId
        shippingParam.isTradein = shipmentDetailData.isTradein
        shippingParam.products = products
        shippingParam.uniqueId = cartString
        shippingParam.isTradeInDropOff = isTradeInDropOff
        shippingParam.preOrderDuration = shipmentDetailData.shipmentCartData!!.preOrderDuration
        shippingParam.isFulfillment = shipmentDetailData.shipmentCartData!!.isFulfillment
        shippingParam.boMetadata = shipmentDetailData.shipmentCartData!!.boMetadata
        if (isTradeInDropOff && recipientAddressModel?.locationDataModel != null) {
            shippingParam.destinationDistrictId = recipientAddressModel.locationDataModel.district
            shippingParam.destinationPostalCode = recipientAddressModel.locationDataModel.postalCode
            shippingParam.destinationLatitude = recipientAddressModel.locationDataModel.latitude
            shippingParam.destinationLongitude = recipientAddressModel.locationDataModel.longitude
        } else {
            shippingParam.destinationDistrictId =
                shipmentDetailData.shipmentCartData!!.destinationDistrictId
            shippingParam.destinationPostalCode =
                shipmentDetailData.shipmentCartData!!.destinationPostalCode
            shippingParam.destinationLatitude =
                shipmentDetailData.shipmentCartData!!.destinationLatitude
            shippingParam.destinationLongitude =
                shipmentDetailData.shipmentCartData!!.destinationLongitude
        }
        shippingParam.groupType = shipmentDetailData.shipmentCartData!!.groupType
        return shippingParam
    }
}
