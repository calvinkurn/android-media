package com.tokopedia.hotel.booking.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by resakemal on 15/05/19
 */

data class HotelCart (
        @SerializedName("cartID")
        @Expose
        val cartID: String = "",

        @SerializedName("cart")
        @Expose
        val cart: HotelCartData = HotelCartData(),

        @SerializedName("property")
        @Expose
        val property: HotelPropertyData = HotelPropertyData(),

        @SerializedName("appliedVoucher")
        @Expose
        var appliedVoucher: AppliedVoucher = AppliedVoucher()
) {
        data class Response (
                @SerializedName("propertyGetCart")
                @Expose
                val response: HotelCart = HotelCart()
        )

        data class AppliedVoucher (
                @SerializedName("code")
                @Expose
                val code: String = "",
                @SerializedName("discount")
                @Expose
                val discount: String = "",
                @SerializedName("discountAmount")
                @Expose
                val discountAmount: Long = 0L,
                @SerializedName("cashback")
                @Expose
                val cashback: String = "",
                @SerializedName("cashbackAmount")
                @Expose
                val cashbackAmount: Long = 0L,
                @SerializedName("message")
                @Expose
                val message: String = "",
                @SerializedName("titleDescription")
                @Expose
                val titleDescription: String = "",
                @SerializedName("isCoupon")
                @Expose
                var isCoupon: Int = 0
        )
}

