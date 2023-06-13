package com.tokopedia.tkpd.flashsale.data.mapper

import com.tokopedia.tkpd.flashsale.data.response.GetFlashSaleProductSubmissionProgressResponse
import com.tokopedia.tkpd.flashsale.domain.entity.FlashSaleProductSubmissionProgress
import javax.inject.Inject

class GetFlashSaleProductSubmissionProgressMapper @Inject constructor() {

    fun map(response: GetFlashSaleProductSubmissionProgressResponse.GetFlashSaleProductSubmissionProgress): FlashSaleProductSubmissionProgress {
        return FlashSaleProductSubmissionProgress(
            mapToListCampaign(response.listCampaign),
            mapToListCampaignProductError(response.listCampaignProductError),
            response.openSse,
            response.listProductHasNext
        )
    }

    private fun mapToListCampaignProductError(
        listCampaignProductError: List<GetFlashSaleProductSubmissionProgressResponse.GetFlashSaleProductSubmissionProgress.CampaignProductError>
    ): List<FlashSaleProductSubmissionProgress.CampaignProductError> {
        return listCampaignProductError.map {
            FlashSaleProductSubmissionProgress.CampaignProductError(
                it.productId,
                it.productName,
                it.sku,
                it.productPicture,
                it.message,
                it.errorType
            )
        }
    }

    private fun mapToListCampaign(
        listCampaign: List<GetFlashSaleProductSubmissionProgressResponse.GetFlashSaleProductSubmissionProgress.Campaign>
    ): List<FlashSaleProductSubmissionProgress.Campaign> {
        return listCampaign.map {
            FlashSaleProductSubmissionProgress.Campaign(
                it.campaignId,
                it.campaignName,
                it.productProcessed,
                it.productSubmitted,
                it.campaignPicture
            )
        }
    }
}
