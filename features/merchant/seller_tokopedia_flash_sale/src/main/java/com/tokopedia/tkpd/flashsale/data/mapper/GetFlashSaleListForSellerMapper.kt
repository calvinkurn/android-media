package com.tokopedia.tkpd.flashsale.data.mapper

import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.tkpd.flashsale.data.response.GetFlashSaleListForSellerResponse
import com.tokopedia.tkpd.flashsale.domain.entity.Campaign
import javax.inject.Inject

class GetFlashSaleListForSellerMapper @Inject constructor() {

    fun map(response: GetFlashSaleListForSellerResponse): List<Campaign> {
        return response.getFlashSaleListForSeller.campaignList.map { campaign ->
            Campaign(
                campaign.campaignId.toLongOrZero(),
                campaign.cancellationReason,
                campaign.coverImage,
                campaign.description,
                campaign.endDateUnix,
                campaign.maxProductSubmission,
                campaign.name,
                campaign.toProductMeta(),
                campaign.remainingQuota,
                campaign.reviewEndDateUnix,
                campaign.reviewStartDateUnix,
                campaign.slug,
                campaign.startDateUnix,
                campaign.statusId.toIntOrZero(),
                campaign.statusText,
                campaign.submissionEndDateUnix,
                campaign.submissionStartDateUnix,
                campaign.useMultilocation
            )
        }
    }

    private fun GetFlashSaleListForSellerResponse.GetFlashSaleListForSeller.Campaign.toProductMeta(): Campaign.ProductMeta {
        return Campaign.ProductMeta(
            productMeta.acceptedProduct,
            productMeta.rejectedProduct,
            productMeta.totalProduct,
            productMeta.totalProductStock,
            productMeta.totalStockSold,
            productMeta.transferredProduct
        )
    }

}