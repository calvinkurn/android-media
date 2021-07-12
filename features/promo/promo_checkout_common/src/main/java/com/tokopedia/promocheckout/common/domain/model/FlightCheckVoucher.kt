package com.tokopedia.promocheckout.common.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FlightCheckVoucher (

        @SerializedName("voucherCode")
        @Expose
        var voucherCode: String = "",
        @SerializedName("TitleDescription")
        @Expose
        var titleDescription: String = "",
        @SerializedName("IsCoupon")
        @Expose
        var isCoupon: Int = 0,
        @SerializedName("UserID")
        @Expose
        var userID: Int = 0,
        @SerializedName("DiscountAmount")
        @Expose
        var discountAmount: String = "",
        @SerializedName("DiscountAmountPlain")
        @Expose
        var discountAmountPlain: Long = 0,
        @SerializedName("CashbackAmount")
        @Expose
        var cashbackAmount: String = "",
        @SerializedName("cashbackAmountPlain")
        @Expose
        var cashbackAmountPlain: Long = 0,
        @SerializedName("DiscountedPrice")
        @Expose
        var discountedPrice: String = "",
        @SerializedName("DiscountedPricePlain")
        @Expose
        var discountedPricePlain: Long = 0,
        @SerializedName("Message")
        @Expose
        var message: String = "",
        @SerializedName("VoucherAmount")
        @Expose
        var voucherAmount: Long = 0,
        var messageColor: String = ""
) {
    class Response(
            @SerializedName("flightVoucher")
            @Expose
            var response: FlightCheckVoucher = FlightCheckVoucher()
    )
}