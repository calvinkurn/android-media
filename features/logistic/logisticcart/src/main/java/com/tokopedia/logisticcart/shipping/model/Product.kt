package com.tokopedia.logisticcart.shipping.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by Irfan Khoirul on 2019-09-30.
 */

@Parcelize
data class Product(
        @SerializedName("product_id")
        var productId: Long = 0,

        @SerializedName("is_free_shipping")
        var isFreeShipping: Boolean = false,

        @SerializedName("is_free_shipping_tc")
        var isFreeShippingTc: Boolean = false
) : Parcelable