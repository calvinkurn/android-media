package com.tokopedia.purchase_platform.common.feature.bmgm.data.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class BmGmProductTier(
    @Expose
    @SerializedName("product_id")
    val productId: String = "",
    @Expose
    @SerializedName("warehouse_id")
    val warehouseId: Long = 0L,
    @Expose
    @SerializedName("qty")
    val qty: Int = 0,
    @Expose
    @SerializedName("final_price")
    val finalPrice: Double = 0.0,
    @Expose
    @SerializedName("price_after_bmgm")
    val priceAfterBmgm: Double = 0.0,
    @Expose
    @SerializedName("cart_id")
    val cartId: String = ""
) : Parcelable
