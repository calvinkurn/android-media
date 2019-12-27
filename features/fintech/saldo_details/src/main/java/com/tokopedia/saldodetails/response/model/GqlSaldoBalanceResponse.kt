package com.tokopedia.saldodetails.response.model

import com.google.gson.annotations.SerializedName

class GqlSaldoBalanceResponse(
        @SerializedName("balance")
        var saldo: Saldo? = null
)

data class Saldo(
        @SerializedName("buyer_hold")
        var buyerHold: Long = 0,

        @SerializedName("buyer_hold_fmt")
        var buyerHoldFmt: String? = null,

        @SerializedName("buyer_usable")
        var buyerUsable: Long = 0,

        @SerializedName("buyer_usable_fmt")
        var buyerUsableFmt: String? = null,

        @SerializedName("seller_hold")
        var sellerHold: Long = 0,

        @SerializedName("seller_hold_fmt")
        var sellerHoldFmt: String? = null,

        @SerializedName("seller_usable")
        var sellerUsable: Long = 0,

        @SerializedName("seller_usable_fmt")
        var sellerUsableFmt: String? = null
)
