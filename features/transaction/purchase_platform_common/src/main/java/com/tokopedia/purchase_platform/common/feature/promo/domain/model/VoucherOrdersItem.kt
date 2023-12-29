package com.tokopedia.purchase_platform.common.feature.promo.domain.model

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class VoucherOrdersItem(
    @SerializedName("code")
    val code: String = "",

    @SerializedName("unique_id")
    val uniqueId: String = "",

    @SerializedName("message")
    val message: Message = Message(),

    @SerializedName("type")
    val type: String = "",

    @SuppressLint("Invalid Data Type")
    @SerializedName("shipping_id")
    val shippingId: Int = 0,

    @SuppressLint("Invalid Data Type")
    @SerializedName("sp_id")
    val spId: Int = 0,

    @SerializedName("cart_string_group")
    val cartStringGroup: String = ""
)
