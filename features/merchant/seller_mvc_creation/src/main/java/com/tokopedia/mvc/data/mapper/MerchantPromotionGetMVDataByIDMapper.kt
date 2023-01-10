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
            val voucherDetailData = VoucherDetailData(
                voucherId = voucherId,
                shopId = shopId,
                voucherName = voucherName,
                voucherType = PromoType.values().firstOrNull { value -> value.id == voucherType }
                    ?: PromoType.FREE_SHIPPING,
                voucherImage = voucherImage,
                voucherImageSquare = voucherImageSquare,
                voucherImagePortrait = voucherImagePortrait,
                voucherStatus = VoucherStatus.values().firstOrNull { value -> value.id == voucherStatus }
                    ?: VoucherStatus.PROCESSING,
                voucherDiscountType = voucherDiscountType.toBenefitType(),
                voucherDiscountAmount = voucherDiscountAmount,
                voucherDiscountAmountMax = voucherDiscountAmountMax,
                voucherDiscountAmountMin = voucherDiscountAmountMin,
                voucherQuota = voucherQuota,
                voucherStartTime = voucherStartTime,
                voucherFinishTime = voucherFinishTime,
                voucherCode = voucherCode,
                galadrielVoucherId = galadrielVoucherId,
                galadrielCatalogId = galadrielCatalogId,
                createTime = createTime,
                createBy = createBy,
                updateTime = updateTime,
                updateBy = updateBy,
                isPublic = isPublic,
                isQuotaAvailable = isQuotaAvailable,
                voucherTypeFormatted = voucherTypeFormatted,
                voucherStatusFormatted = voucherStatusFormatted,
                voucherDiscountTypeFormatted = voucherDiscountTypeFormatted,
                voucherDiscountAmountFormatted = voucherDiscountAmountFormatted,
                voucherDiscountAmountMaxFormatted = voucherDiscountAmountMaxFormatted,
                remainingQuota = remainingQuota,
                tnc = tnc,
                bookedGlobalQuota = bookedGlobalQuota,
                confirmedGlobalQuota = confirmedGlobalQuota,
                targetBuyer = VoucherTargetBuyer.values().firstOrNull { value -> value.id == targetBuyer }
                    ?: VoucherTargetBuyer.ALL_BUYER,
                minimumTierLevel = minimumTierLevel,
                isVoucherProduct = isLockToProduct == TRUE,
                isVps = isVps,
                packageName = packageName,
                vpsUniqueId = vpsUniqueId,
                voucherPackageId = voucherPackageId,
                vpsBundlingId = vpsBundlingId,
                isSubsidy = isSubsidy,
                appLink = appLink,
                webLink = webLink,
                warehouseId = warehouseId,
                voucherMinimumAmountType = voucherMinimumAmountType,
                voucherMinimumAmountTypeFormatted = voucherMinimumAmountTypeFormatted,
                isPeriod = isPeriod,
                totalPeriod = totalPeriod,
                voucherLockType = voucherLockType,
                voucherLockId = voucherLockId,
                productIds = toProductIds()
            )
            voucherDetailData
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
