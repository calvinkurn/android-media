package com.tokopedia.shopdiscount.select.data.mapper

import com.tokopedia.shopdiscount.bulk.data.response.GetSlashPriceBenefitResponse
import com.tokopedia.shopdiscount.select.domain.entity.ShopBenefit
import javax.inject.Inject

class ShopBenefitMapper @Inject constructor() {

    fun map(input: GetSlashPriceBenefitResponse): ShopBenefit {
        val benefits = input.getSlashPriceBenefit.slashPriceBenefits.map {
            ShopBenefit.Benefit(
                it.expiredAt,
                it.expiredAtUnix,
                it.maxQuota,
                it.packageId,
                it.packageName,
                it.remainingQuota,
                it.shopGrade,
                it.shopTier
            )
        }
        return ShopBenefit(input.getSlashPriceBenefit.isUseVps, benefits)
    }
}
