package com.tokopedia.tkpd.flashsale.data.mapper

import com.tokopedia.campaign.utils.constant.DateConstant
import com.tokopedia.kotlin.extensions.view.formatTo
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.tkpd.flashsale.data.response.GetFlashSaleListForSellerResponse
import com.tokopedia.tkpd.flashsale.domain.entity.FlashSale
import com.tokopedia.tkpd.flashsale.domain.entity.FlashSaleData
import com.tokopedia.tkpd.flashsale.domain.entity.enums.FlashSaleListPageTab
import com.tokopedia.tkpd.flashsale.domain.entity.enums.FlashSaleStatus
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

    fun map(response: GetFlashSaleListForSellerResponse): FlashSaleData {
        return FlashSaleData(
            response.getFlashSaleListForSeller.totalCampaign,
            response.toFlashSale()
        )
    }

    private fun GetFlashSaleListForSellerResponse.toFlashSale(): List<FlashSale> {
        return getFlashSaleListForSeller.campaignList.map { flashSale ->
            FlashSale(
                flashSale.campaignId.toLongOrZero(),
                flashSale.cancellationReason,
                flashSale.coverImage,
                flashSale.description,
                flashSale.endDateUnix.epochToDate(),
                flashSale.maxProductSubmission,
                flashSale.name,
                flashSale.hasEligibleProduct,
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
                flashSale.toCampaignStatus(),
                flashSale.toProductCriteria(),
                flashSale.toTabName()
            )
        }
    }


    private fun GetFlashSaleListForSellerResponse.GetFlashSaleListForSeller.Campaign.toCampaignStatus(): FlashSaleStatus {
        return when (statusId.toIntOrZero()) {
            FLASH_SALE_STATUS_ID_UPCOMING -> FlashSaleStatus.UPCOMING
            FLASH_SALE_STATUS_ID_NO_REGISTERED_PRODUCT -> FlashSaleStatus.NO_REGISTERED_PRODUCT
            FLASH_SALE_STATUS_ID_WAITING_FOR_SELECTION -> FlashSaleStatus.WAITING_FOR_SELECTION
            FLASH_SALE_STATUS_ID_ON_SELECTION_PROCESS -> FlashSaleStatus.ON_SELECTION_PROCESS
            FLASH_SALE_STATUS_ID_SELECTION_FINISHED -> FlashSaleStatus.SELECTION_FINISHED
            FLASH_SALE_STATUS_ID_ONGOING -> FlashSaleStatus.ONGOING
            FLASH_SALE_STATUS_ID_FINISHED -> FlashSaleStatus.FINISHED
            FLASH_SALE_STATUS_ID_CANCELLED -> FlashSaleStatus.CANCELLED
            FLASH_SALE_STATUS_ID_REJECTED -> FlashSaleStatus.REJECTED
            FLASH_SALE_STATUS_ID_MISSED -> FlashSaleStatus.MISSED
            else -> FlashSaleStatus.UPCOMING
        }
    }

    private fun GetFlashSaleListForSellerResponse.GetFlashSaleListForSeller.Campaign.toProductMeta(): FlashSale.ProductMeta {
        return FlashSale.ProductMeta(
            productMeta.acceptedProduct,
            productMeta.rejectedProduct,
            productMeta.totalProduct,
            productMeta.totalProductStock,
            productMeta.totalStockSold,
            productMeta.transferredProduct,
            productMeta.totalSoldValue
        )
    }

    private fun GetFlashSaleListForSellerResponse.GetFlashSaleListForSeller.Campaign.formatDate(): FlashSale.FormattedDate {
        return FlashSale.FormattedDate(
            startDateUnix.epochToDate()
                .formatTo(DateConstant.DATE_TIME_SECOND_PRECISION_WITH_TIMEZONE_ID_FORMAT),
            endDateUnix.epochToDate()
                .formatTo(DateConstant.DATE_TIME_SECOND_PRECISION_WITH_TIMEZONE_ID_FORMAT)
        )
    }

    private fun GetFlashSaleListForSellerResponse.GetFlashSaleListForSeller.Campaign.toProductCriteria(): List<FlashSale.ProductCriteria> {
        return productCriteria.map { productCriteria ->
            FlashSale.ProductCriteria(
                productCriteria.criteriaId,
                productCriteria.minPrice,
                productCriteria.maxPrice,
                productCriteria.minFinalPrice,
                productCriteria.maxFinalPrice,
                productCriteria.minDiscount,
                productCriteria.minCustomStock,
                productCriteria.maxCustomStock,
                productCriteria.minRating,
                productCriteria.minProductScore,
                productCriteria.minQuantitySold,
                productCriteria.maxQuantitySold,
                productCriteria.maxSubmission,
                productCriteria.maxProductAppear,
                productCriteria.dayPeriodTimeAppear,
                productCriteria.toProductCategories(),
                productCriteria.additionalInfo.toFLashSaleAdditionalInfo()
            )
        }
    }

    private fun GetFlashSaleListForSellerResponse.GetFlashSaleListForSeller.Campaign.AdditionalInfo.toFLashSaleAdditionalInfo(): FlashSale.AdditionalInfo {
        return FlashSale.AdditionalInfo(
            matchedProduct
        )
    }

    private fun GetFlashSaleListForSellerResponse.GetFlashSaleListForSeller.Campaign.ProductCriteria.toProductCategories(): List<FlashSale.ProductCategories> {
        return categories.map { category ->
            FlashSale.ProductCategories(
                category.categoryId,
                category.categoryName
            )
        }
    }

    private fun GetFlashSaleListForSellerResponse.GetFlashSaleListForSeller.Campaign.toTabName(): FlashSaleListPageTab {
        return when (statusId.toIntOrZero()) {
            FLASH_SALE_STATUS_ID_UPCOMING -> FlashSaleListPageTab.UPCOMING
            FLASH_SALE_STATUS_ID_NO_REGISTERED_PRODUCT -> FlashSaleListPageTab.REGISTERED
            FLASH_SALE_STATUS_ID_WAITING_FOR_SELECTION -> FlashSaleListPageTab.REGISTERED
            FLASH_SALE_STATUS_ID_ON_SELECTION_PROCESS -> FlashSaleListPageTab.REGISTERED
            FLASH_SALE_STATUS_ID_SELECTION_FINISHED -> FlashSaleListPageTab.REGISTERED
            FLASH_SALE_STATUS_ID_ONGOING -> FlashSaleListPageTab.ONGOING
            FLASH_SALE_STATUS_ID_FINISHED -> FlashSaleListPageTab.FINISHED
            FLASH_SALE_STATUS_ID_CANCELLED -> FlashSaleListPageTab.FINISHED
            FLASH_SALE_STATUS_ID_REJECTED -> FlashSaleListPageTab.FINISHED
            FLASH_SALE_STATUS_ID_MISSED -> FlashSaleListPageTab.FINISHED
            else -> FlashSaleListPageTab.UPCOMING
        }
    }
}
