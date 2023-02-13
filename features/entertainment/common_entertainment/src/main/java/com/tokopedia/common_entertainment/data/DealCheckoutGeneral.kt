package com.tokopedia.common_entertainment.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

open class DealsGeneral
open class DealsInstant

data class DealCheckoutGeneral(
        @SerializedName("carts")
        @Expose
        val carts : CartsPromo = CartsPromo()
): DealsGeneral()

data class DealCheckoutGeneralInstant(
        @SerializedName("carts")
        @Expose
        val carts : CartsPromo = CartsPromo(),
        @SerializedName("gateway_code")
        @Expose
        var gatewayCode : String = ""
): DealsInstant()

data class DealCheckoutGeneralNoPromo(
        @SerializedName("carts")
        @Expose
        val carts : Carts = Carts()
): DealsGeneral()

data class DealCheckoutGeneralInstantNoPromo(
        @SerializedName("carts")
        @Expose
        val carts : Carts = Carts(),
        @SerializedName("gateway_code")
        @Expose
        var gatewayCode : String = ""
): DealsInstant()

data class Carts(
        @SerializedName("business_type")
        @Expose
        var businessType: Int = 0,
        @SerializedName("cart_info")
        @Expose
        var cartInfo: MutableList<CartInfo> = arrayListOf()
)

data class CartsPromo(
        @SerializedName("business_type")
        @Expose
        var businessType: Int = 0,
        @SerializedName("cart_info")
        @Expose
        var cartInfo: MutableList<CartInfo> = arrayListOf(),
        @SerializedName("promo_codes")
        @Expose
        var promoCodes: List<String> = emptyList()
)

data class CartInfo(
        @SerializedName("metadata")
        @Expose
        var metaData: String = "",
        @SerializedName("data_type")
        @Expose
        var dataType: String = ""
)
