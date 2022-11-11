package com.tokopedia.mvc.data.mapper

import com.tokopedia.mvc.data.response.MerchantPromotionGetMVDataByIDResponse
import com.tokopedia.mvc.domain.entity.VoucherDetailData
import javax.inject.Inject

class MerchantPromotionGetMVDataByIDMapper @Inject constructor() {

    fun map(response: MerchantPromotionGetMVDataByIDResponse): VoucherDetailData {
        return with(response.merchantPromotionGetMVDataByID.data) {
            VoucherDetailData(
                voucherId,
                shopId,
                voucherName,
                voucherType,
                voucherImage,
                voucherImageSquare,
                voucherImagePortrait,
                voucherStatus,
                voucherDiscountType,
                voucherDiscountAmount,
                voucherDiscountAmountMax,
                voucherDiscountAmountMin,
                voucherQuota,
                voucherStartTime,
                voucherFinishTime,
                voucherCode,
                galadrielVoucherId,
                galadrielCatalogId,
                createTime,
                createBy,
                updateTime,
                updateBy,
                isPublic,
                isQuotaAvailable,
                voucherTypeFormatted,
                voucherStatusFormatted,
                voucherDiscountTypeFormatted,
                voucherDiscountAmountFormatted,
                voucherDiscountAmountMaxFormatted,
                remainingQuota,
                voucherMinimumAmountFormatted,
                tnc,
                bookedGlobalQuota,
                confirmedGlobalQuota,
                targetBuyer,
                minimumTierLevel,
                isLockToProduct,
                isVps,
                packageName,
                vpsUniqueId,
                voucherPackageId,
                vpsBundlingId,
                isSubsidy,
                appLink,
                webLink,
                warehouseId,
                voucherMinimumAmountType,
                voucherMinimumAmountTypeFormatted,
                isPeriod,
                totalPeriod,
                voucherLockType,
                voucherLockId,
                toProductIds()
            )
        }
    }

    private fun MerchantPromotionGetMVDataByIDResponse.MerchantPromotionGetMVDataByID.Data.toProductIds(): List<VoucherDetailData.ProductId> {
        return productIds.map {
            VoucherDetailData.ProductId(
                it.parentProductId,
                it.chilProductId
            )
        }
    }
}
