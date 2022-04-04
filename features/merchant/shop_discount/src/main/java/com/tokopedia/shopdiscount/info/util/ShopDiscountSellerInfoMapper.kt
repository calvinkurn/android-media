package com.tokopedia.shopdiscount.info.util

import com.tokopedia.shopdiscount.bulk.data.response.GetSlashPriceBenefitResponse
import com.tokopedia.shopdiscount.info.data.uimodel.ShopDiscountSellerInfoUiModel

object ShopDiscountSellerInfoMapper {

    fun mapToShopDiscountSellerInfoBenefitUiModel(
        response: GetSlashPriceBenefitResponse
    ): ShopDiscountSellerInfoUiModel {
        return ShopDiscountSellerInfoUiModel(
            responseHeader = response.getSlashPriceBenefit.responseHeader,
            isUseVps = response.getSlashPriceBenefit.isUseVps,
            listSlashPriceBenefitData = mapToListSlashPriceBenefitData(response.getSlashPriceBenefit.slashPriceBenefits)
        )
    }

    private fun mapToListSlashPriceBenefitData(
        slashPriceBenefits: List<GetSlashPriceBenefitResponse.GetSlashPriceBenefit.SlashPriceBenefit>
    ): List<ShopDiscountSellerInfoUiModel.SlashPriceBenefitData> {
        return slashPriceBenefits.map {
            ShopDiscountSellerInfoUiModel.SlashPriceBenefitData(
                packageId = it.packageId,
                packageName = it.packageName,
                remainingQuota = it.remainingQuota,
                maxQuota = it.maxQuota,
                expiredAt = it.expiredAt,
                expiredAtUnix = it.expiredAtUnix
            )
        }
    }

}