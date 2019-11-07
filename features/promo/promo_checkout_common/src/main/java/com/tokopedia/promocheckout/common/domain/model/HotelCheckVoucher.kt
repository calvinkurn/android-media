package com.tokopedia.promocheckout.common.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class HotelCheckVoucher (

        @SerializedName("code")
        @Expose
        var voucherCode: String = "",
        @SerializedName("titleDescription")
        @Expose
        var titleDescription: String = "",
        @SerializedName("isCoupon")
        @Expose
        var isCoupon: Int = 0,
        @SerializedName("discount")
        @Expose
        var discountAmount: String = "",
        @SerializedName("discountAmount")
        @Expose
        var discountAmountPlain: Long = 0,
        @SerializedName("cashback")
        @Expose
        var cashbackAmount: String = "",
        @SerializedName("cashbackAmount")
        @Expose
        var cashbackAmountPlain: Long = 0,
        @SerializedName("message")
        @Expose
        var message: String = ""

) {
    class Response(
            @SerializedName("data")
            @Expose
            var response: HotelCheckVoucher = HotelCheckVoucher()
    )
}