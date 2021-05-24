package com.tokopedia.flight.booking.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 2019-11-08
 */

data class FlightVerify(
        @SerializedName("cartItems")
        @Expose
        val cartItems: List<FlightVerifyCart> = listOf(),

        @SerializedName("promo")
        @Expose
        val promo: Promo = Promo()

) {
    data class Response(
            @SerializedName("flightVerify")
            @Expose
            val flightVerify: FlightVerifyMetaAndData = FlightVerifyMetaAndData()
    )

    data class FlightVerifyMetaAndData(
            @SerializedName("data")
            @Expose
            val data: FlightVerify = FlightVerify(),

            @SerializedName("meta")
            @Expose
            val meta: Meta = Meta()
    )

    data class Meta(
            @SerializedName("needRefresh")
            @Expose
            val needRefresh: Boolean = false,

            @SerializedName("refreshTime")
            @Expose
            val refreshTime: Int = 0,

            @SerializedName("maxRetry")
            @Expose
            val maxRetry: Int = 0
    )

    data class FlightVerifyCart(
            @SerializedName("productId")
            @Expose
            val productId: Int = 0,

            @SerializedName("quantity")
            @Expose
            val quantity: Int = 0,

            @SerializedName("metaData")
            @Expose
            val metaData: CartMetaData = CartMetaData(),

            @SerializedName("configuration")
            val configuration: CartConfiguration = CartConfiguration(),

            @SerializedName("newPrice")
            @Expose
            val newPrice: List<FlightCart.NewPrice> = listOf(),

            @SerializedName("oldPrice")
            @Expose
            val oldPrice: String = "",

            @SerializedName("oldPriceNumeric")
            @Expose
            val oldPriceNumeric: Int = 0,

            @SerializedName("priceDetail")
            @Expose
            val priceDetail: List<FlightCart.PriceDetail> = listOf(),

            var promoEligibility: PromoEligibility = PromoEligibility()
    )

    data class PromoEligibility(
            var message: String = "",
            var success: Boolean = false
    )

    data class CartMetaData(
            @SerializedName("cartID")
            @Expose
            val cartId: String = "",

            @SerializedName("invoiceID")
            @Expose
            val invoiceId: String = ""
    )

    data class CartConfiguration(
            @SerializedName("price")
            @Expose
            var price: Int = 0
    )

    data class Promo(
            @SerializedName("code")
            @Expose
            val code: String = "",

            @SerializedName("discount")
            @Expose
            val discount: String = "",

            @SerializedName("discountNumeric")
            @Expose
            val discountNumeric: Int = 0,

            @SerializedName("cashback")
            @Expose
            val cashback: String = "",

            @SerializedName("cashbackNumeric")
            @Expose
            val cashbackNumeric: Int = 0,

            @SerializedName("message")
            @Expose
            val message: String = ""
    )


}