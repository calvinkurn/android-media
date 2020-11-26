package com.tokopedia.promocheckout.common.domain.model.deals

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.promocheckout.common.domain.model.event.CartItem
import com.tokopedia.promocheckout.common.domain.model.event.User

data class Cart (
        @SerializedName("cart_items")
        @Expose
        val cartItems: List<CartItem> = emptyList(),
        @SerializedName("total_conv_fee")
        @Expose
        val totalConvFee: Int = 0,
        @SerializedName("total_price")
        @Expose
        val totalPrice: Int = 0,
        @SerializedName("grand_total")
        @Expose
        val grandTotal: Int = 0,
        @SerializedName("display_price")
        @Expose
        val displayPrice: Int = 0,
        @SerializedName("promocode")
        @Expose
        val promocode: String = "",
        @SerializedName("promocode_discount")
        @Expose
        val promocodeDiscount: Int = 0,
        @SerializedName("promocode_cashback")
        @Expose
        val promocodeCashback: Int = 0,
        @SerializedName("cashback_amount")
        @Expose
        val cashbackAmount: Int = 0,
        @SerializedName("promocode_failure_message")
        @Expose
        val promocodeFailureMessage: String = "",
        @SerializedName("promocode_success_message")
        @Expose
        val promocodeSuccessMessage: String = "",
        @SerializedName("promocode_status")
        @Expose
        val promocodeStatus: String = "",
        @SerializedName("count")
        @Expose
        val count: Int = 0,
        @SerializedName("user")
        @Expose
        val user: User = User(),
        @SerializedName("order_title")
        @Expose
        val orderTitle: String = "",
        @SerializedName("bank_code")
        @Expose
        val bankCode: String = "",
        @SerializedName("order_subtitle")
        @Expose
        val orderSubtitle: String = ""
)