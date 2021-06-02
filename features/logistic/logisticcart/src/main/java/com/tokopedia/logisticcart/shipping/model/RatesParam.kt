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
        val actualWeight: String?,
        var token: String = "",
        var ut: String = "",
        val type: String = VALUE_ANDROID,
        val from: String = VALUE_CLIENT,
        var insurance: String = "",
        var product_insurance: String = "",
        var order_value: String = "",
        var cat_id: String = "",
        val lang: String = VALUE_LANG_ID,
        var user_history: Int = -1,
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
        var used_otdg: Int? = null,
        var occ: String = "0",
        var po_time: Int = 0,
        var is_fulfillment: Boolean = false,
        var mvc: String = "",
        var bo_metadata: String = ""
) {

    private constructor(builder: Builder) : this(
            spids = builder.spids,
            origin = builder.origin,
            destination = builder.destination,
            weight = builder.weight,
            actualWeight = builder.weightActual,
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
            unique_id = builder.unique_id,
            is_fulfillment = builder.is_fulfillment,
            po_time = builder.po_time,
            mvc = builder.mvc,
            bo_metadata = builder.bo_metadata)

    fun toMap(): Map<String, Any?> = mapOf(
            "spids" to spids,
            "shop_id" to shop_id,
            "origin" to origin,
            "destination" to destination,
            "weight" to weight,
            "actual_weight" to actualWeight,
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
            "unique_id" to unique_id,
            "occ" to occ,
            "mvc" to mvc,
            "po_time" to po_time,
            "is_fulfillment" to is_fulfillment,
            "bo_metadata" to bo_metadata
    )

    class Builder(val shopShipments: List<ShopShipment>, val shipping: ShippingParam) {

        var spids: String = RatesParamHelper.generateSpIds(shopShipments)
            private set
        var origin: String = RatesParamHelper.generateOrigin(shipping)
            private set
        var destination: String = RatesParamHelper.generateDestination(shipping)
            private set
        var weight: String = shipping.weightInKilograms.toString()
            private set
        var weightActual: String? = if (shipping.weightActualInKilograms > 0) shipping.weightActualInKilograms.toString() else null
            private set
        var trade_in: Int = RatesParamHelper.determineTradeIn(shipping)
            private set
        var is_corner = 0
            private set
        var shop_id: String = shipping.shopId
            private set
        var token: String = shipping.token
            private set
        var ut: String = shipping.ut
            private set
        var insurance: String = shipping.insurance.toString()
            private set
        var product_insurance: String = shipping.productInsurance.toString()
            private set
        var order_value: String = shipping.orderValue.toString()
            private set
        var cat_id: String = shipping.categoryIds
            private set
        var user_history: Int = -1
            private set
        var is_blackbox: Int = if (shipping.isBlackbox) 1 else 0
            private set
        var address_id: String = shipping.addressId ?: "0"
            private set
        var preorder: Int = if (shipping.isPreorder) 1 else 0
            private set
        var vehicle_leasing: Int = 0
            private set
        var psl_code: String = ""
            private set
        var products: String = RatesParamHelper.generateProducts(shipping)
            private set
        var unique_id: String = shipping.uniqueId
            private set
        var is_fulfillment: Boolean = shipping.isFulfillment
            private set
        var po_time: Int = shipping.preOrderDuration
            private set
        var mvc: String = ""
        var bo_metadata: String = RatesParamHelper.generateBoMetadata(shipping.boMetadata)
            private set

        fun isCorner(is_corner: Boolean) = apply { this.is_corner = if (is_corner) 1 else 0 }

        fun codHistory(history: Int) = apply { this.user_history = history }

        fun isLeasing(leasing: Boolean) = apply { this.vehicle_leasing = if (leasing) 1 else 0 }

        fun promoCode(code: String?) = apply { this.psl_code = code ?: "" }

        fun mvc(mvc: String?) = apply { this.mvc = mvc ?: "" }

        fun build() = RatesParam(this)

    }

}