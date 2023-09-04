package com.tokopedia.purchase_platform.common.feature.bmgm.data.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class BmGmProduct(
    @Expose
    @SerializedName("product_id")
    val productId: String = "",
    @Expose
    @SerializedName("warehouse_id")
    val warehouseId: Long = 0L,
    @Expose
    @SerializedName("quantity")
    val quantity: Int = 0,
    @Expose
    @SerializedName("price_before_benefit")
    val priceBeforeBenefit: Double = 0.0,
    @Expose
    @SerializedName("price_after_benefit")
    val priceAfterBenefit: Double = 0.0,
    @Expose
    @SerializedName("cart_id")
    val cartId: String = ""
) : Parcelable
