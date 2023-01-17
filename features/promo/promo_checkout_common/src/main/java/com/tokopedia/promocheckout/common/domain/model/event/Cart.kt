package com.tokopedia.promocheckout.common.domain.model.event

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Cart(
        @SerializedName("bank_code")
        @Expose
        val bankCode: String = "",
        @SerializedName("error")
        @Expose
        val error: String = "",
        @SerializedName("cart_items")
        @Expose
        val cartItems: List<CartItem> = emptyList(),
        @SerializedName("cashback_amount")
        @Expose
        val cashbackAmount: Long = 0L,
        @SerializedName("count")
        @Expose
        val count: Int = 0,
        @SerializedName("display_price")
        @Expose
        val displayPrice: Double = 0.0,
        @SerializedName("grand_total")
        @Expose
        val grandTotal: Int = 0,
        @SerializedName("order_subtitle")
        @Expose
        val orderSubtitle: String = "",
        @SerializedName("order_title")
        @Expose
        val orderTitle: String = "",
        @SerializedName("promocode")
        @Expose
        val promocode: String = "",
        @SerializedName("promocode_cashback")
        @Expose
        val promocodeCashback: Long = 0L,
        @SerializedName("promocode_discount")
        @Expose
        val promocodeDiscount: Long = 0L,
        @SerializedName("promocode_failure_message")
        @Expose
        val promocodeFailureMessage: String = "",
        @SerializedName("promocode_status")
        @Expose
        val promocodeStatus: String = "",
        @SerializedName("promocode_success_message")
        @Expose
        val promocodeSuccessMessage: String = "",
        @SerializedName("total_conv_fee")
        @Expose
        val totalConvFee: Int = 0,
        @SerializedName("total_price")
        @Expose
        val totalPrice: Double = 0.0,
        @SerializedName("user")
        @Expose
        val user: User = User()
)