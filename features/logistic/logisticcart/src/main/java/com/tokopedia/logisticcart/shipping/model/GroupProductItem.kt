package com.tokopedia.logisticcart.shipping.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class GroupProductItem(
    @SerializedName("product_id")
    val productId: Long = 0,
    @SerializedName("order_value")
    val orderValue: Long = 0,
    @SerializedName("weight")
    val weight: String = "0",
    @SerializedName("warehouse_ids")
    val warehouseIds: List<Long> = emptyList()
) : Parcelable
