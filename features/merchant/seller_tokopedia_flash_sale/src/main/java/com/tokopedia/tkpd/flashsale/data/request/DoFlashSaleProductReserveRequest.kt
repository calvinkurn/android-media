package com.tokopedia.tkpd.flashsale.data.request

import com.google.gson.annotations.SerializedName
import com.tokopedia.tkpd.flashsale.data.request.CampaignParticipationRequestHeader

data class DoFlashSaleProductReserveRequest(
    @SerializedName("action")
    val action: Int,
    @SerializedName("campaign_id")
    val campaignId: Long,
    @SerializedName("reservation_id")
    val reservationId: String,
    @SerializedName("product_data")
    val productData: List<ProductData>
) {
    data class ProductData (
        @SerializedName("product_id")
        val productId: Long,
        @SerializedName("criteria_id")
        val criteriaId: Long
    )
}