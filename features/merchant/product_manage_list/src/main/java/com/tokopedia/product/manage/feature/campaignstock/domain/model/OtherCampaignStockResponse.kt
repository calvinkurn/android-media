package com.tokopedia.product.manage.feature.campaignstock.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.product.manage.feature.campaignstock.ui.util.CampaignStockMapper

data class OtherCampaignStockResponse(
        @SerializedName("getProductV3")
        val otherCampaignStockData: OtherCampaignStockData = OtherCampaignStockData()
)

data class OtherCampaignStockData(
        @SerializedName("pictures")
        val pictureList: List<CampaignStockPicture> = listOf(),
        @SerializedName("status")
        val status: String = "",
        @SerializedName("stock")
        val stock: Int = 0
){

        val isActive = status == CampaignStockMapper.ACTIVE
}

data class CampaignStockPicture(
        @SerializedName("urlThumbnail")
        val urlThumbnail: String = ""
)