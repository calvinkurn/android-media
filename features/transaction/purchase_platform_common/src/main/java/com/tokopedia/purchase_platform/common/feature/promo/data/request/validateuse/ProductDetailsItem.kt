package com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductDetailsItem(

    @field:SerializedName("quantity")
    var quantity: Int = -1,

    @SuppressLint("Invalid Data Type")
    @field:SerializedName("product_id")
    var productId: Long = -1,

    @SerializedName("bundle_id")
    var bundleId: Long = 0
) : Parcelable
