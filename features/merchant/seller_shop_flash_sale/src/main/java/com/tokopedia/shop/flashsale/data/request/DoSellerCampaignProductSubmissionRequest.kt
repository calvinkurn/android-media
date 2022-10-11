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
        @SuppressLint("Invalid Data Type")
        @SerializedName("final_price")
        val finalPrice: Long,
        @SerializedName("custom_stock")
        val customStock: Long,
        @SerializedName("teaser")
        val teaser: Teaser,
        @SerializedName("warehouses")
        val warehouses: List<Warehouse>,
        @SerializedName("max_order")
        val maxOrder: Int
    ) {
        data class Teaser(
            @SerializedName("active")
            val active: Boolean,
            @SerializedName("position")
            val position: Int
        )
        data class Warehouse(
            @SuppressLint("Invalid Data Type")
            @SerializedName("warehouse_id")
            val warehouseId: Long,
            @SerializedName("custom_stock")
            val customStock: Long
        )
    }
}
