package com.tokopedia.purchase_platform.common.feature.bmgm.data.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class BmGmTierProductBenefit(
    @Expose
    @SerializedName("product_id")
    val productId: String,
    @Expose
    @SerializedName("quantity")
    val quantity: Int,
    @Expose
    @SerializedName("stock")
    val stock: Int,
    @Expose
    @SerializedName("product_name")
    val productName: String,
    @Expose
    @SerializedName("product_cache_image_url")
    val productCacheImageUrl: String,
    @Expose
    @SerializedName("original_price")
    val originalPrice: Double,
    @Expose
    @SerializedName("final_price")
    val finalPrice: Double
): Parcelable
