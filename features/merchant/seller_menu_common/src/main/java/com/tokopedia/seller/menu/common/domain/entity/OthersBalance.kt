package com.tokopedia.seller.menu.common.domain.entity

import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted

data class OtherBalanceResponse(
        @SerializedName("balance")
        val othersBalance: OthersBalance = OthersBalance()
)

data class OthersBalance (
        @SerializedName("seller_usable")
        var sellerBalance: Float = 0f,
        @SerializedName("buyer_usable")
        var buyerBalance: Float = 0f) {

        val totalBalance: String?
                get() {
                        val total = sellerBalance + buyerBalance
                        return total.getCurrencyFormatted()
                }
}