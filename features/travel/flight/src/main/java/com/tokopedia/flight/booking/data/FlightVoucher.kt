package com.tokopedia.flight.booking.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 2019-11-12
 */

data class FlightVoucher(
        @SerializedName("voucherCode")
        @Expose
        val voucherCode: String = "",

        @SerializedName("UserID")
        @Expose
        val userId: Int = 0,

        @SerializedName("DiscountAmount")
        @Expose
        val discountAmount: String = "",

        @SerializedName("DiscountAmountPlain")
        @Expose
        val discountAmountPlain: Int = 0,

        @SerializedName("CashbackAmount")
        @Expose
        val cashbackAmount: String = "",

        @SerializedName("cashbackAmountPlain")
        @Expose
        val cashbackAmountPlain: Int = 0,

        @SerializedName("DiscountedPrice")
        @Expose
        val discountedPrice: String = "",

        @SerializedName("DiscountedPricePlain")
        @Expose
        val discountedPricePlain: Int = 0,

        @SerializedName("Message")
        @Expose
        val message: String = "",

        @SerializedName("VoucherAmount")
        @Expose
        val voucherAmount: Int = 0,

        @SerializedName("TitleDescription")
        @Expose
        val titleDescription: String = "",

        @SerializedName("IsCoupon")
        @Expose
        val isCoupon: Int = 0

) {
    data class Response(
            @SerializedName("flightVoucher")
            @Expose
            val flightVoucher: FlightVoucher = FlightVoucher()
    )
}