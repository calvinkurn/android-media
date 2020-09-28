package com.tokopedia.home.account.revamp.domain.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DepositDataModel(
        @Expose
        @SerializedName("buyer_usable")
        var buyerUsable: Long = 0,
        @Expose
        @SerializedName("seller_usable")
        val sellerUsable: Long = 0
) {
    val deposit: Long
    get() = buyerUsable + sellerUsable
}