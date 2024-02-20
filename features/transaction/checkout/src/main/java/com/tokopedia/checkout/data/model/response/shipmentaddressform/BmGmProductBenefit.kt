package com.tokopedia.checkout.data.model.response.shipmentaddressform

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class BmGmProductBenefit(
    @SerializedName("product_id")
    val productId: String = "",
    @SerializedName("quantity")
    val quantity: Int = 0,
    @SerializedName("product_name")
    val productName: String = "",
    @SerializedName("product_cache_image_url")
    val productImage: String = "",
    @SerializedName("original_price")
    val originalPrice: Double = 0.0,
    @SerializedName("final_price")
    val finalPrice: Double = 0.0,
    @SerializedName("weight")
    val weight: Int = 0,
    @SerializedName("actual_weight")
    val actualWeight: Int = 0
) : Parcelable
