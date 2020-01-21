package com.tokopedia.logisticcart.shipping.model

import com.tokopedia.logisticcart.utils.RatesParamHelper

private const val VALUE_ANDROID = "android"
private const val VALUE_CLIENT = "client"
private const val VALUE_LANG_ID = "id"

data class RatesParam(
        val spids: String,
        var shop_id: String = "",
        val origin: String,
        val destination: String,
        val weight: String,
        var token: String = "",
        var ut: String = "",
        val type: String = VALUE_ANDROID,
        val from: String = VALUE_CLIENT,
        var insurance: String = "",
        var product_insurance: String = "",
        var order_value: String = "",
        var cat_id: String = "",
        val lang: String = VALUE_LANG_ID,
        var user_history: Int = 0,
        var is_corner: Int = 0,
        var is_blackbox: Int = 0,
        var address_id: String = "",
        var preorder: Int = 0,
        var trade_in: Int = 0,
        var vehicle_leasing: Int = 0,
        var psl_code: String = "",
        var products: String = "",
        var unique_id: String = "",
        var corner_id: Int? = null,
        var pdp: String? = null,
        var loadtest: Int? = null,
        var is_express_checkout: Int? = null,
        var val_rates: String? = null,
        var used_otdg: Int? = null
) {

    private constructor(builder: Builder) : this(
            spids = builder.spids,
            origin = builder.origin,
            destination = builder.destination,
            weight = builder.weight,
            trade_in = builder.trade_in,
            is_corner = builder.is_corner,
            shop_id = builder.shop_id,
            token = builder.token,
            ut = builder.ut,
            insurance = builder.insurance,
            product_insurance = builder.product_insurance,
            order_value = builder.order_value,
            cat_id = builder.cat_id,
            user_history = builder.user_history,
            is_blackbox = builder.is_blackbox,
            address_id = builder.address_id,
            preorder = builder.preorder,
            vehicle_leasing = builder.vehicle_leasing,
            psl_code = builder.psl_code,
            products = builder.products,
            unique_id = builder.unique_id)

    fun toMap(): Map<String, Any> = mapOf(
            "spids" to spids,
            "shop_id" to shop_id,
            "origin" to origin,
            "destination" to destination,
            "weight" to weight,
            "token" to token,
            "ut" to ut,
            "type" to type,
            "from" to from,
            "insurance" to insurance,
            "product_insurance" to product_insurance,
            "order_value" to order_value,
            "cat_id" to cat_id,
            "lang" to lang,
            "user_history" to user_history,
            "is_corner" to is_corner,
            "is_blackbox" to is_blackbox,
            "address_id" to address_id,
            "preorder" to preorder,
            "trade_in" to trade_in,
            "vehicle_leasing" to vehicle_leasing,
            "psl_code" to psl_code,
            "products" to products,
            "unique_id" to unique_id
    )

    class Builder(val shopShipments: List<ShopShipment>, val shipping: ShippingParam) {

        val spids: String
        val origin: String
        val destination: String
        val weight: String
        val trade_in: Int
        var is_corner: Int = 0
        val shop_id: String
        val token: String
        val ut: String
        val insurance: String
        val product_insurance: String
        val order_value: String
        val cat_id: String
        var user_history: Int = 0
        val is_blackbox: Int
        val address_id: String
        val preorder: Int
        var vehicle_leasing: Int = 0
        var psl_code: String = ""
        val products: String
        val unique_id: String

        init {
            this.spids = RatesParamHelper.generateSpIds(shopShipments)
            this.origin = RatesParamHelper.generateOrigin(shipping)
            this.destination = RatesParamHelper.generateDestination(shipping)
            this.weight = shipping.weightInKilograms.toString()
            this.trade_in = RatesParamHelper.determineTradeIn(shipping)
            this.shop_id = shipping.shopId
            this.token = shipping.token
            this.ut = shipping.ut
            this.insurance = shipping.insurance.toString()
            this.product_insurance = shipping.productInsurance.toString()
            this.order_value = shipping.orderValue.toString()
            this.cat_id = shipping.categoryIds
            this.is_blackbox = if (shipping.isBlackbox) 1 else 0
            this.address_id = shipping.addressId.toString()
            this.preorder = if (shipping.isPreorder) 1 else 0
            // todo: verify this later on
            this.products = RatesParamHelper.generateProducts(shipping)
            this.unique_id = shipping.uniqueId
        }

        fun isCorner(is_corner: Boolean) = apply { this.is_corner = if (is_corner) 1 else 0 }

        fun codHistory(history: Int) = apply { this.user_history = history }

        fun isLeasing(leasing: Boolean) = apply { this.vehicle_leasing = if (leasing) 1 else 0 }

        fun promoCode(code: String?) = apply { this.psl_code = code ?: "" }

        fun build() = RatesParam(this)

    }

}