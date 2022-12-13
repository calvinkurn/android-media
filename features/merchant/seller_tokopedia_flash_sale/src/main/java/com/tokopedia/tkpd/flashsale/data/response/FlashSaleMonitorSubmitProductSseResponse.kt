package com.tokopedia.tkpd.flashsale.data.response

import com.google.gson.annotations.SerializedName

data class FlashSaleMonitorSubmitProductSseResponse(
    @SerializedName("reservation_id")
    val reservationId: String = "",
    @SerializedName("status")
    val status: String = "",
    @SerializedName("count_processed_products")
    val countProcessedProduct: Int = 0,
    @SerializedName("count_all_products")
    val countAllProduct: Int = 0,
    @SerializedName("campaign_id")
    val campaignId: String = ""
)
