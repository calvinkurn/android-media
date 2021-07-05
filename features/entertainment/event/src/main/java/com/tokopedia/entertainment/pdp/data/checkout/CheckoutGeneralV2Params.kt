package com.tokopedia.entertainment.pdp.data.checkout

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CheckoutGeneralV2Params(
        @SerializedName("carts")
        @Expose
        val carts : Carts = Carts()
)

data class CheckoutGeneralV2InstantParams(
        @SerializedName("carts")
        @Expose
        val carts : Carts = Carts(),
        @SerializedName("gateway_code")
        @Expose
        val gatewayCode : String = ""
)

data class Carts(
        @SerializedName("business_type")
        @Expose
        var businessType: Int = 0,
        @SerializedName("cart_info")
        @Expose
        var cartInfo: MutableList<CartInfo> = arrayListOf()
)

data class CartInfo(
        @SerializedName("metadata")
        @Expose
        var metaData: String = "",
        @SerializedName("data_type")
        @Expose
        var dataType: String = ""
)