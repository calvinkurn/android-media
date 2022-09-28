package com.tokopedia.shop.flashsale.data.request

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class BannerGeneratorDataRequest(
    @SuppressLint("Invalid Data Type")
    @SerializedName("CampaignID")
    val campaignId: Long,
    @SerializedName("Rows")
    val rows: Int
)