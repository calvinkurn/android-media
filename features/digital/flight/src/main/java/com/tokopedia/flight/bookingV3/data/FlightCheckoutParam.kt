package com.tokopedia.flight.bookingV3.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 2019-11-08
 */

data class FlightCheckoutParam(
        @SerializedName("cartItems")
        @Expose
        val cartItems: List<CartItem> = listOf(),

        @SerializedName("promoCode")
        @Expose
        val promoCode: String = ""
) {
    data class CartItem(
            @SerializedName("productID")
            @Expose
            val productId: Int = 0,

            @SerializedName("quantity")
            @Expose
            val quantity: Int = 0,

            @SerializedName("metaData")
            @Expose
            val metaData: MetaData = MetaData(),

            @SerializedName("configuration")
            @Expose
            val configuration: FlightVerify.CartConfiguration = FlightVerify.CartConfiguration()
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