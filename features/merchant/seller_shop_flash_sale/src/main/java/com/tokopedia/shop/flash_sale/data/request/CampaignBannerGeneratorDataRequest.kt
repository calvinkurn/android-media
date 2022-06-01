package com.tokopedia.shop.flash_sale.data.request

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class CampaignBannerGeneratorDataRequest(
    @SuppressLint("Invalid Data Type")
    @SerializedName("CampaignID")
    val campaignId: Long,
    @SerializedName("Rows")
    val rows: Int
)