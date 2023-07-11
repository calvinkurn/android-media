package com.tokopedia.logisticcart.shipping.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class GroupProduct(
    @SerializedName("shop_id")
    val shopId: Long = 0,
    @SerializedName("total_weight")
    val totalWeight: String = "0",
    @SerializedName("total_order_value")
    val totalOrderValue: Long = 0,
    @SerializedName("warehouse_id")
    val warehouseId: Long = 0,
    @SerializedName("unique_id")
    val uniqueId: String = "", // this is cart string order
    @SerializedName("products")
    val products: List<GroupProductItem> = emptyList()
) : Parcelable
