package com.tokopedia.checkout.data.model.request.saveshipmentstate

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class ShipmentStateShopProductData(
        @SuppressLint("Invalid Data Type")
        @SerializedName("shop_id")
        var shopId: Long = 0,
        @SerializedName("is_preorder")
        var isPreorder: Int = 0,
        @SuppressLint("Invalid Data Type")
        @SerializedName("warehouse_id")
        var warehouseId: Long = 0,
        @SerializedName("finsurance")
        var finsurance: Int = 0,
        @SerializedName("shipping_info")
        var shippingInfoData: ShipmentStateShippingInfoData? = null,
        @SerializedName("is_dropship")
        var isDropship: Int = 0,
        @SerializedName("dropship_data")
        var dropshipData: ShipmentStateDropshipData? = null,
        @SerializedName("is_order_priority")
        var isOrderPriority: Int = 0,
        @SerializedName("product_data")
        var productDataList: List<ShipmentStateProductData>? = null
)