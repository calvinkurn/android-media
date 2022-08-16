package com.tokopedia.tkpd.flashsale.data.mapper

import com.tokopedia.campaign.utils.constant.DateConstant
import com.tokopedia.kotlin.extensions.view.formatTo
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.tkpd.flashsale.data.response.GetFlashSaleListForSellerResponse
import com.tokopedia.tkpd.flashsale.domain.entity.FlashSale
import com.tokopedia.tkpd.flashsale.util.extension.epochToDate
import com.tokopedia.tkpd.flashsale.util.extension.removeTimeZone
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class GetFlashSaleListForSellerMapper @Inject constructor() {

    fun map(response: GetFlashSaleListForSellerResponse): List<FlashSale> {
        return response.getFlashSaleListForSeller.campaignList.map { flashSale ->
            FlashSale(
                flashSale.campaignId.toLongOrZero(),
                flashSale.cancellationReason,
                flashSale.coverImage,
                flashSale.description,
                flashSale.endDateUnix.epochToDate().removeTimeZone(),
                flashSale.maxProductSubmission,
                flashSale.name,
                flashSale.toProductMeta(),
                flashSale.remainingQuota,
                flashSale.reviewEndDateUnix.epochToDate().removeTimeZone(),
                flashSale.reviewStartDateUnix.epochToDate().removeTimeZone(),
                flashSale.slug,
                flashSale.startDateUnix.epochToDate().removeTimeZone(),
                flashSale.statusId.toIntOrZero(),
                flashSale.statusText,
                flashSale.submissionEndDateUnix.epochToDate().removeTimeZone(),
                flashSale.submissionStartDateUnix.epochToDate(),
                flashSale.useMultilocation,
                flashSale.formatDate(),
                flashSale.hoursDifference()
            )
        }
    }

    private fun GetFlashSaleListForSellerResponse.GetFlashSaleListForSeller.Campaign.hoursDifference(): Int {
        val startDate = startDateUnix.epochToDate()
        val endDate = endDateUnix.epochToDate()
        val differenceInMillis = endDate.time - startDate.time
        return TimeUnit.MILLISECONDS.toHours(differenceInMillis).toInt()
    }

    private fun GetFlashSaleListForSellerResponse.GetFlashSaleListForSeller.Campaign.toProductMeta(): FlashSale.ProductMeta {
        return FlashSale.ProductMeta(
            productMeta.acceptedProduct,
            productMeta.rejectedProduct,
            productMeta.totalProduct,
            productMeta.totalProductStock,
            productMeta.totalStockSold,
            productMeta.transferredProduct
        )
    }

    private fun GetFlashSaleListForSellerResponse.GetFlashSaleListForSeller.Campaign.formatDate(): FlashSale.FormattedDate {
        return FlashSale.FormattedDate(
            startDateUnix.epochToDate().formatTo(DateConstant.DATE_TIME_SECOND_PRECISION_WITH_TIMEZONE_ID_FORMAT),
            endDateUnix.epochToDate().formatTo(DateConstant.DATE_TIME_SECOND_PRECISION_WITH_TIMEZONE_ID_FORMAT),
            submissionStartDateUnix.epochToDate().formatTo(DateConstant.DATE_TIME_SECOND_PRECISION_WITH_TIMEZONE_ID_FORMAT),
            submissionEndDateUnix.epochToDate().formatTo(DateConstant.DATE_TIME_SECOND_PRECISION_WITH_TIMEZONE_ID_FORMAT),
            reviewStartDateUnix.epochToDate().formatTo(DateConstant.DATE_TIME_SECOND_PRECISION_WITH_TIMEZONE_ID_FORMAT),
            reviewEndDateUnix.epochToDate().formatTo(DateConstant.DATE_TIME_SECOND_PRECISION_WITH_TIMEZONE_ID_FORMAT)
        )
    }
}