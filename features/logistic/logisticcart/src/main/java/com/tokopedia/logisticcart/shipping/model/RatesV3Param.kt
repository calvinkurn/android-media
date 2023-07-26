package com.tokopedia.logisticcart.shipping.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class RatesV3Param(
    @SerializedName("param")
    val data: OngkirRatesV3Input,
    @SerializedName("metadata")
    val metadata: RatesMetadata
) : GqlParam

data class RatesV3ApiParam(
    @SerializedName("param")
    val data: OngkirRatesV3Input
) : GqlParam

data class OngkirRatesV3Input(
    @SerializedName("spids")
    val spids: String,
    @SerializedName("shop_id")
    var shop_id: String = "",
    @SerializedName("shop_tier")
    var shop_tier: Int = 0,
    @SerializedName("origin")
    val origin: String,
    @SerializedName("destination")
    val destination: String,
    @SerializedName("weight")
    val weight: String,
    @SerializedName("actual_weight")
    val actualWeight: String?,
    @SerializedName("token")
    var token: String = "",
    @SerializedName("ut")
    var ut: String = "",
    @SerializedName("type")
    val type: String = "",
    @SerializedName("from")
    val from: String = "",
    @SerializedName("insurance")
    var insurance: String = "",
    @SerializedName("product_insurance")
    var product_insurance: String = "",
    @SerializedName("order_value")
    var order_value: String = "",
    @SerializedName("cat_id")
    var cat_id: String = "",
    @SerializedName("lang")
    val lang: String = "",
    @SerializedName("user_history")
    var user_history: Int = -1,
    @SerializedName("is_corner")
    var is_corner: Int = 0,
    @SerializedName("is_blackbox")
    var is_blackbox: Int = 0,
    @SerializedName("address_id")
    var address_id: String = "",
    @SerializedName("preorder")
    var preorder: Int = 0,
    @SerializedName("trade_in")
    var trade_in: Int = 0,
    @SerializedName("vehicle_leasing")
    var vehicle_leasing: Int = 0,
    @SerializedName("psl_code")
    var psl_code: String = "",
    @SerializedName("products")
    var products: String = "",
    @SerializedName("unique_id")
    var unique_id: String = "",
    @SerializedName("po_time")
    var po_time: Int = 0,
    @SerializedName("occ")
    var occ: String = "0",
    @SerializedName("is_fulfillment")
    var is_fulfillment: Boolean = false,
    @SerializedName("mvc")
    var mvc: String = "",
    @SerializedName("bo_metadata")
    var bo_metadata: String = "",
    @SerializedName("cart_data")
    var cart_data: String = "",
    @SerializedName("warehouse_id")
    var warehouse_id: String = "",
    // new owoc
    @SerializedName("group_type")
    val group_type: Int = 0
) : GqlParam

data class RatesMetadata(@SerializedName("cart_data") val cartData: String) : GqlParam
