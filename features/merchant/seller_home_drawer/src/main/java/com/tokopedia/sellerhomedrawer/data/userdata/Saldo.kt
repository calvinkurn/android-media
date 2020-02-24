package com.tokopedia.sellerhomedrawer.data.userdata

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Saldo (

    @SerializedName("buyer_usable")
    @Expose
    var buyerUsable: Long = 0,
    @SerializedName("seller_usable")
    @Expose
    var sellerUsable: Long = 0) {

    val deposit: Long
        get() = buyerUsable + sellerUsable
}

