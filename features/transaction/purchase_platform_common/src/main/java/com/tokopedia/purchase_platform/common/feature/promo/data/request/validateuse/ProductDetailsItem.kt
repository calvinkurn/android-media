package com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductDetailsItem(

        @field:SerializedName("quantity")
        var quantity: Int = -1,

        @field:SerializedName("product_id")
        var productId: Long = -1
) : Parcelable