package com.tokopedia.checkout.data.model.request.saveshipmentstate

import com.google.gson.annotations.SerializedName

data class ShipmentStateShopProductData(
    @SerializedName("cart_string_group")
    var cartStringGroup: String = "",
    @SerializedName("is_preorder")
    var isPreorder: Int = 0,
    @SerializedName("warehouse_id")
    var warehouseId: Long = 0,
    @SerializedName("finsurance")
    var finsurance: Int = 0,
    @SerializedName("shipping_info")
    var shippingInfoData: ShipmentStateShippingInfoData = ShipmentStateShippingInfoData(),
    @SerializedName("is_dropship")
    var isDropship: Int = 0,
    @SerializedName("dropship_data")
    var dropshipData: ShipmentStateDropshipData = ShipmentStateDropshipData(),
    @SerializedName("is_order_priority")
    var isOrderPriority: Int = 0,
    @SerializedName("product_data")
    var productDataList: List<ShipmentStateProductData> = emptyList(),
    @SerializedName("validation_metadata")
    var validationMetadata: String = ""
)
