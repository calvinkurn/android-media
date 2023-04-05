package com.tokopedia.logisticcart.utils

import com.google.gson.Gson
import com.tokopedia.logisticcart.shipping.model.ShippingParam
import com.tokopedia.logisticcart.shipping.model.ShopShipment
import com.tokopedia.purchase_platform.common.feature.bometadata.BoMetadata

internal object RatesParamHelper {

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

    fun generateGroupProducts(shipping: ShippingParam): String {
        val gson = Gson()
        val json = gson.toJson(shipping.groupProducts)
        return json.replace("\n", "")
    }
}
