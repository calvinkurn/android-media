package com.tokopedia.logisticcart.shipping.model

import com.tokopedia.logisticcart.utils.RatesParamHelper

private const val VALUE_ANDROID = "android"
private const val VALUE_CLIENT = "client"
private const val VALUE_CARTAPP = "cartapp"
private const val VALUE_LANG_ID = "id"

data class RatesParam(
    val spids: String,
    var shop_id: String = "",
    var shop_tier: Int = 0,
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
    var bo_metadata: String = "",
    var cart_data: String = ""
) {

    private constructor(builder: Builder) : this(
        spids = builder.spids,
        origin = builder.origin,
        destination = builder.destination,
        weight = builder.weight,
        actualWeight = builder.weightActual,
        trade_in = builder.tradeIn,
        is_corner = builder.isCorner,
        shop_id = builder.shopId,
        shop_tier = builder.shopTier,
        token = builder.token,
        ut = builder.ut,
        insurance = builder.insurance,
        product_insurance = builder.productInsurance,
        order_value = builder.orderValue,
        cat_id = builder.catId,
        user_history = builder.userHistory,
        is_blackbox = builder.isBlackbox,
        address_id = builder.addressId,
        preorder = builder.preorder,
        vehicle_leasing = builder.vehicleLeasing,
        psl_code = builder.pslCode,
        products = builder.products,
        unique_id = builder.uniqueId,
        is_fulfillment = builder.isFulfillment,
        po_time = builder.poTime,
        mvc = builder.mvc,
        bo_metadata = builder.boMetadata,
        cart_data = builder.cartData,
        occ = builder.occ
    )

    fun toMap(): Map<String, Any?> = mapOf(
        "spids" to spids,
        "shop_id" to shop_id,
        "shop_tier" to shop_tier,
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

    fun toMetadata(): Map<String, Any?> = mapOf(
        "cart_data" to cart_data
    )

    fun toBoAffordabilityMap(appVersion: String): Map<String, Any?> = mapOf(
        "origin" to origin,
        "destination" to destination,
        "source" to VALUE_CARTAPP,
        "type" to type,
        "device_version" to "$VALUE_ANDROID-$appVersion",
        "weight" to weight,
        "actual_weight" to actualWeight,
        "order_value" to order_value,
        "spids" to spids,
        "shop_id" to shop_id,
        "shop_tier" to shop_tier,
        "user_history" to user_history,
        "is_fulfillment" to is_fulfillment,
        "bo_metadata" to bo_metadata,
        "products" to products,
        "psl_code" to psl_code,
        "unique_id" to unique_id
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
        var weightActual: String = shipping.weightActualInKilograms.toString()
            private set
        var tradeIn: Int = RatesParamHelper.determineTradeIn(shipping)
            private set
        var isCorner = 0
            private set
        var shopId: String = shipping.shopId ?: ""
            private set
        var shopTier: Int = shipping.shopTier
            private set
        var token: String = shipping.token ?: ""
            private set
        var ut: String = shipping.ut ?: ""
            private set
        var insurance: String = shipping.insurance.toString()
            private set
        var productInsurance: String = shipping.productInsurance.toString()
            private set
        var orderValue: String = shipping.orderValue.toString()
            private set
        var catId: String = shipping.categoryIds ?: ""
            private set
        var userHistory: Int = -1
            private set
        var isBlackbox: Int = if (shipping.isBlackbox) 1 else 0
            private set
        var addressId: String = shipping.addressId ?: "0"
            private set
        var preorder: Int = if (shipping.isPreorder) 1 else 0
            private set
        var vehicleLeasing: Int = 0
            private set
        var pslCode: String = ""
            private set
        var products: String = RatesParamHelper.generateProducts(shipping)
            private set
        var uniqueId: String = shipping.uniqueId ?: ""
            private set
        var isFulfillment: Boolean = shipping.isFulfillment
            private set
        var poTime: Int = shipping.preOrderDuration
            private set
        var mvc: String = ""
        var boMetadata: String = RatesParamHelper.generateBoMetadata(shipping.boMetadata)
            private set
        var occ: String = "0"
        var cartData: String = ""
            private set

        fun isCorner(is_corner: Boolean) = apply { this.isCorner = if (is_corner) 1 else 0 }

        fun codHistory(history: Int) = apply { this.userHistory = history }

        fun isLeasing(leasing: Boolean) = apply { this.vehicleLeasing = if (leasing) 1 else 0 }

        fun promoCode(code: String?) = apply { this.pslCode = code ?: "" }

        fun mvc(mvc: String?) = apply { this.mvc = mvc ?: "" }

        fun isOcc(isOcc: Boolean) = apply { this.occ = if (isOcc) "1" else "0" }

        fun cartData(cart_data: String) = apply { this.cartData = cart_data }

        fun build() = RatesParam(this)
    }
}
