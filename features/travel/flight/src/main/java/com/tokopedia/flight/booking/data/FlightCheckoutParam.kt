package com.tokopedia.flight.booking.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 2019-11-08
 */

data class FlightCheckoutParam(
        @SerializedName("cartItems")
        @Expose
        val cartItems: MutableList<CartItem> = mutableListOf(),

        @SerializedName("promoCode")
        @Expose
        var promoCode: String = ""
) {
    data class CartItem(
            @SerializedName("productID")
            @Expose
            var productId: Int = 0,

            @SerializedName("quantity")
            @Expose
            var quantity: Int = 0,

            @SerializedName("metaData")
            @Expose
            var metaData: MetaData = MetaData(),

            @SerializedName("configuration")
            @Expose
            var configuration: FlightVerify.CartConfiguration = FlightVerify.CartConfiguration()
    )

    data class MetaData(
            @SerializedName("cartID")
            @Expose
            val cartId: String = "",

            @SerializedName("invoiceID")
            @Expose
            val invoiceId: String = "",

            @SerializedName("ipAddress")
            @Expose
            val ipAddress: String = "",

            @SerializedName("userAgent")
            @Expose
            val userAgent: String = "",

            @SerializedName("did")
            @Expose
            val did: Int = 0
    )
}