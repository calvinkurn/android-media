package com.tokopedia.product.manage.feature.campaignstock.domain.model.response

import com.google.gson.annotations.SerializedName
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus

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

        fun getIsActive() =
                status == ProductStatus.ACTIVE.name
}

data class CampaignStockPicture(
        @SerializedName("urlThumbnail")
        val urlThumbnail: String = ""
)