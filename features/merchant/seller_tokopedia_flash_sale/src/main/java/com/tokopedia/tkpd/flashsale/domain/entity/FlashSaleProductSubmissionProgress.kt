package com.tokopedia.tkpd.flashsale.domain.entity

data class FlashSaleProductSubmissionProgress(
    val listCampaign: List<Campaign> = listOf(),
    val listCampaignProductError: List<CampaignProductError> = listOf(),
    val isOpenSse: Boolean = false,
    val isHasNextProduct: Boolean = false
){
    data class Campaign(
        val campaignId: String = "",
        val campaignName: String = "",
        val productProcessed: Int = 0,
        val productSubmitted: Int = 0,
        val campaignPicture: String = ""
    )

    data class CampaignProductError(
        val productId: String = "",
        val productName: String = "",
        val sku: String = "",
        val productPicture: String = "",
        val message: String = "",
        val errorType: String = ""
    )

}
