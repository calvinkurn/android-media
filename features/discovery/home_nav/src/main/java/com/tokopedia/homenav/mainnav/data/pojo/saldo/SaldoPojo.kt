package com.tokopedia.homenav.mainnav.data.pojo.saldo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SaldoPojo(
        @SerializedName("balance")
        @Expose
        var saldo: Saldo = Saldo(),
        var isError: Boolean = false,
        var errorString: String = ""
) {

    data class Saldo(
            @SerializedName("buyer_hold")
            @Expose
            var buyerHold: Long = 0,

            @SerializedName("buyer_hold_fmt")
            @Expose
            var buyerHoldFmt: String ="",

            @SerializedName("buyer_usable")
            @Expose
            var buyerUsable: Long = 0,

            @SerializedName("buyer_usable_fmt")
            @Expose
            var buyerUsableFmt: String = "",

            @SerializedName("seller_hold")
            @Expose
            var sellerHold: Long = 0,

            @SerializedName("seller_hold_fmt")
            @Expose
            var sellerHoldFmt: String = "",

            @SerializedName("seller_usable")
            @Expose
            var sellerUsable: Long = 0,

            @SerializedName("seller_usable_fmt")
            @Expose
            var sellerUsableFmt: String = ""
    )
}
