package com.tokopedia.seller.action.balance.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted

data class SellerActionBalanceResponse(
        @SerializedName("balance")
        val balance: Balance = Balance()
)

data class Balance(
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