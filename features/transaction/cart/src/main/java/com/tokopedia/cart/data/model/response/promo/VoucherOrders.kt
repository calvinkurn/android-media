package com.tokopedia.cart.data.model.response.promo

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 09/03/20.
 */
data class VoucherOrders(
    @SerializedName("code")
    var code: String = "",

    @SerializedName("unique_id")
    var uniqueId: String = "",

    @SerializedName("message")
    var message: MessageVoucherOrders = MessageVoucherOrders(),

    @SerializedName("cart_id")
    val cartId: String = "",

    @SuppressLint("Invalid Data Type")
    @SerializedName("shipping_id")
    val shippingId: Int = 0,

    @SuppressLint("Invalid Data Type")
    @SerializedName("sp_id")
    val spId: Int = 0,

    @SerializedName("type")
    val type: String = "",

    @SerializedName("shipping_price")
    val shippingPrice: Double = 0.0,

    @SerializedName("shipping_subsidy")
    val shippingSubsidy: Long = 0,

    @SerializedName("benefit_class")
    val benefitClass: String = "",

    @SerializedName("bo_campaign_id")
    val boCampaignId: String = "",

    @SerializedName("eta_txt")
    val etaText: String = "",

    @SerializedName("cart_string_group")
    val cartStringGroup: String = ""
) {
    companion object {
        private const val TYPE_LOGISTIC = "logistic"
    }

    fun isTypeLogistic(): Boolean {
        return type == TYPE_LOGISTIC
    }
}
