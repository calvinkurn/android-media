package com.tokopedia.checkout.data.model.request.saveshipmentstate

import com.google.gson.annotations.SerializedName

data class ShipmentStateShopProductData(
        @SerializedName("shop_id")
        var shopId: Int = 0,
        @SerializedName("is_preorder")
        var isPreorder: Int = 0,
        @SerializedName("warehouse_id")
        var warehouseId: Int = 0,
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