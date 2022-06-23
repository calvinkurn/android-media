package com.tokopedia.shop.flashsale.data.request

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class DoSellerCampaignProductSubmissionRequest(
    @SuppressLint("Invalid Data Type") // BE still using number
    @SerializedName("campaign_id")
    val campaignId: Long,
    @SerializedName("action")
    val action: Int,
    @SerializedName("product_data")
    val productData: List<ProductData>
) {
    data class ProductData (
        @SuppressLint("Invalid Data Type") // BE still using number
        @SerializedName("product_id")
        val productId: Long,
        @SerializedName("final_price")
        val finalPrice: Long,
        @SerializedName("custom_stock")
        val customStock: Long,
        @SerializedName("warehouses")
        val warehouses: List<Warehouse> = emptyList(),
        @SerializedName("max_order")
        val maxOrder: Int
    ) {
        data class Warehouse(
            @SuppressLint("Invalid Data Type")
            @SerializedName("warehouse_id")
            val warehouseId: Long,
            @SerializedName("custom_stock")
            val customStock: Int
        )
    }
}
