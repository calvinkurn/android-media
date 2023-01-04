package com.tokopedia.mvc.data.mapper

import com.tokopedia.mvc.data.response.MerchantPromotionGetMVDataByIDResponse
import com.tokopedia.mvc.domain.entity.VoucherDetailData
import com.tokopedia.mvc.domain.entity.enums.BenefitType
import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.VoucherStatus
import com.tokopedia.mvc.domain.entity.enums.VoucherTargetBuyer
import com.tokopedia.mvc.util.constant.DiscountTypeConstant
import javax.inject.Inject

class MerchantPromotionGetMVDataByIDMapper @Inject constructor() {

    companion object {
        private const val TRUE = 1
    }

    fun map(response: MerchantPromotionGetMVDataByIDResponse): VoucherDetailData {
        return with(response.merchantPromotionGetMVDataByID.data) {
            VoucherDetailData(
                voucherId,
                shopId,
                voucherName,
                PromoType.values().firstOrNull { value -> value.id == voucherType }
                    ?: PromoType.FREE_SHIPPING,
                voucherImage,
                voucherImageSquare,
                voucherImagePortrait,
                VoucherStatus.values().firstOrNull { value -> value.id == voucherStatus }
                    ?: VoucherStatus.PROCESSING,
                voucherDiscountType.toBenefitType(),
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
                tnc,
                bookedGlobalQuota,
                confirmedGlobalQuota,
                VoucherTargetBuyer.values().firstOrNull { value -> value.id == targetBuyer }
                    ?: VoucherTargetBuyer.ALL_BUYER,
                minimumTierLevel,
                isLockToProduct == TRUE,
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

    private fun Int.toBenefitType(): BenefitType {
        return when (this) {
            DiscountTypeConstant.NOMINAL -> BenefitType.NOMINAL
            DiscountTypeConstant.PERCENTAGE -> BenefitType.PERCENTAGE
            else -> BenefitType.NOMINAL
        }
    }
}
