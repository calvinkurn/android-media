package com.tokopedia.tkpd.flashsale.data.mapper

import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.tkpd.flashsale.data.response.GetFlashSaleListForSellerResponse
import com.tokopedia.tkpd.flashsale.domain.entity.Campaign
import javax.inject.Inject
import com.tokopedia.tkpd.flashsale.util.extension.epochToDate
import com.tokopedia.tkpd.flashsale.util.extension.removeTimeZone

class GetFlashSaleListForSellerMapper @Inject constructor() {

    fun map(response: GetFlashSaleListForSellerResponse): List<Campaign> {
        return response.getFlashSaleListForSeller.campaignList.map { campaign ->
            Campaign(
                campaign.campaignId.toLongOrZero(),
                campaign.cancellationReason,
                campaign.coverImage,
                campaign.description,
                campaign.endDateUnix.epochToDate().removeTimeZone(),
                campaign.maxProductSubmission,
                campaign.name,
                campaign.toProductMeta(),
                campaign.remainingQuota,
                campaign.reviewEndDateUnix.epochToDate().removeTimeZone(),
                campaign.reviewStartDateUnix.epochToDate().removeTimeZone(),
                campaign.slug,
                campaign.startDateUnix.epochToDate().removeTimeZone(),
                campaign.statusId.toIntOrZero(),
                campaign.statusText,
                campaign.submissionEndDateUnix.epochToDate().removeTimeZone(),
                campaign.submissionStartDateUnix.epochToDate().removeTimeZone(),
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