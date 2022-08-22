package com.tokopedia.tkpd.flashsale.data.mapper

import com.tokopedia.campaign.utils.constant.DateConstant
import com.tokopedia.kotlin.extensions.view.formatTo
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.tkpd.flashsale.data.response.GetFlashSaleListForSellerResponse
import com.tokopedia.tkpd.flashsale.domain.entity.FlashSale
import com.tokopedia.tkpd.flashsale.domain.entity.FlashSaleStatusEnum
import com.tokopedia.tkpd.flashsale.util.constant.FlashSaleStatusConstant.FLASH_SALE_STATUS_ID_CANCELLED
import com.tokopedia.tkpd.flashsale.util.constant.FlashSaleStatusConstant.FLASH_SALE_STATUS_ID_FINISHED
import com.tokopedia.tkpd.flashsale.util.constant.FlashSaleStatusConstant.FLASH_SALE_STATUS_ID_MISSED
import com.tokopedia.tkpd.flashsale.util.constant.FlashSaleStatusConstant.FLASH_SALE_STATUS_ID_NO_REGISTERED_PRODUCT
import com.tokopedia.tkpd.flashsale.util.constant.FlashSaleStatusConstant.FLASH_SALE_STATUS_ID_ONGOING
import com.tokopedia.tkpd.flashsale.util.constant.FlashSaleStatusConstant.FLASH_SALE_STATUS_ID_ON_SELECTION_PROCESS
import com.tokopedia.tkpd.flashsale.util.constant.FlashSaleStatusConstant.FLASH_SALE_STATUS_ID_REJECTED
import com.tokopedia.tkpd.flashsale.util.constant.FlashSaleStatusConstant.FLASH_SALE_STATUS_ID_SELECTION_FINISHED
import com.tokopedia.tkpd.flashsale.util.constant.FlashSaleStatusConstant.FLASH_SALE_STATUS_ID_UPCOMING
import com.tokopedia.tkpd.flashsale.util.constant.FlashSaleStatusConstant.FLASH_SALE_STATUS_ID_WAITING_FOR_SELECTION
import com.tokopedia.tkpd.flashsale.util.extension.epochToDate
import javax.inject.Inject

class GetFlashSaleListForSellerMapper @Inject constructor() {

    fun map(response: GetFlashSaleListForSellerResponse): List<FlashSale> {
        return response.getFlashSaleListForSeller.campaignList.map { flashSale ->
            FlashSale(
                flashSale.campaignId.toLongOrZero(),
                flashSale.cancellationReason,
                flashSale.coverImage,
                flashSale.description,
                flashSale.endDateUnix.epochToDate(),
                flashSale.maxProductSubmission,
                flashSale.name,
                flashSale.toProductMeta(),
                flashSale.remainingQuota,
                flashSale.reviewEndDateUnix.epochToDate(),
                flashSale.reviewStartDateUnix.epochToDate(),
                flashSale.slug,
                flashSale.startDateUnix.epochToDate(),
                flashSale.statusId.toIntOrZero(),
                flashSale.statusText,
                flashSale.submissionEndDateUnix.epochToDate(),
                flashSale.submissionStartDateUnix.epochToDate(),
                flashSale.useMultilocation,
                flashSale.formatDate(),
                flashSale.toCampaignStatus()
            )
        }
    }

    private fun GetFlashSaleListForSellerResponse.GetFlashSaleListForSeller.Campaign.toCampaignStatus(): FlashSaleStatusEnum {
        return when (statusId.toIntOrZero()) {
            FLASH_SALE_STATUS_ID_UPCOMING -> FlashSaleStatusEnum.UPCOMING
            FLASH_SALE_STATUS_ID_NO_REGISTERED_PRODUCT -> FlashSaleStatusEnum.NO_REGISTERED_PRODUCT
            FLASH_SALE_STATUS_ID_WAITING_FOR_SELECTION -> FlashSaleStatusEnum.WAITING_FOR_SELECTION
            FLASH_SALE_STATUS_ID_ON_SELECTION_PROCESS -> FlashSaleStatusEnum.ON_SELECTION_PROCESS
            FLASH_SALE_STATUS_ID_SELECTION_FINISHED -> FlashSaleStatusEnum.SELECTION_FINISHED
            FLASH_SALE_STATUS_ID_ONGOING -> FlashSaleStatusEnum.ONGOING
            FLASH_SALE_STATUS_ID_FINISHED -> FlashSaleStatusEnum.FINISHED
            FLASH_SALE_STATUS_ID_CANCELLED -> FlashSaleStatusEnum.CANCELLED
            FLASH_SALE_STATUS_ID_REJECTED -> FlashSaleStatusEnum.REJECTED
            FLASH_SALE_STATUS_ID_MISSED -> FlashSaleStatusEnum.MISSED
            else -> FlashSaleStatusEnum.UPCOMING
        }
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