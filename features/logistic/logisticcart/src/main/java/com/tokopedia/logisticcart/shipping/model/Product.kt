package com.tokopedia.logisticcart.shipping.model

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Created by Irfan Khoirul on 2019-09-30.
 */
@Parcelize
data class Product(
    @SerializedName("product_id")
    @SuppressLint("Invalid Data Type")
    var productId: Long = 0,

    @SerializedName("is_free_shipping")
    var isFreeShipping: Boolean = false,

    @SerializedName("is_free_shipping_tc")
    var isFreeShippingTc: Boolean = false,

    @SerializedName("shop_id")
    var shopId: Long = 0
) : Parcelable
