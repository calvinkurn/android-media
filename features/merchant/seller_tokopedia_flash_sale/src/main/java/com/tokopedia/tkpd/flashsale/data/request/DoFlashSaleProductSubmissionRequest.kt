package com.tokopedia.tkpd.flashsale.data.request


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class DoFlashSaleProductSubmissionRequest(
    @SerializedName("request_header")
    val requestHeader: CampaignParticipationRequestHeader = CampaignParticipationRequestHeader(),
    @SerializedName("campaign_id")
    val campaignId: Long,
    @SerializedName("product_data")
    val productData: List<ProductData>,
    @SerializedName("reservation_id")
    val reservationId: String
) {
    data class ProductData(
        @SerializedName("criteria_id")
        val criteriaId: Long,
        @SerializedName("product_id")
        val productId: Long,
        @SerializedName("warehouses")
        val warehouses: List<Warehouse>
    ) {
        data class Warehouse(
            @SerializedName("warehouse_id")
            val warehouseId: Long,
            @SuppressLint("Invalid Data Type")
            @SerializedName("discounted_price")
            val discountedPrice: Long,
            @SerializedName("campaign_stock")
            val campaignStock: Long
        )
    }
}
