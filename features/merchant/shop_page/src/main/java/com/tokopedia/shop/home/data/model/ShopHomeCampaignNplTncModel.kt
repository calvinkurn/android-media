package com.tokopedia.shop.home.data.model

import com.google.gson.annotations.SerializedName

data class ShopHomeCampaignNplTncModel(
    @SerializedName("title")
    val title: String = "",

    @SerializedName("messages")
    val listMessage: List<String> = listOf()

) {
    data class Response(
        @SerializedName("getMerchantCampaignTNC")
        val campaignTnc: ShopHomeCampaignNplTncModel = ShopHomeCampaignNplTncModel()
    )
}
