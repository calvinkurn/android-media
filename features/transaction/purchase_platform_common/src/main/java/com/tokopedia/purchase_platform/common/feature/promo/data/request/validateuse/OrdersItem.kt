package com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrdersItem(
        @SuppressLint("Invalid Data Type")
        @SerializedName("shipping_id")
        var shippingId: Int = 0,
        @SuppressLint("Invalid Data Type")
        @SerializedName("shop_id")
        var shopId: Long = 0,
        @SerializedName("codes")
        var codes: MutableList<String> = mutableListOf(),
        @SerializedName("unique_id")
        var uniqueId: String = "",
        @SuppressLint("Invalid Data Type")
        @SerializedName("sp_id")
        var spId: Int = 0,
        @SerializedName("product_details")
        var productDetails: List<ProductDetailsItem?> = listOf()
) : Parcelable