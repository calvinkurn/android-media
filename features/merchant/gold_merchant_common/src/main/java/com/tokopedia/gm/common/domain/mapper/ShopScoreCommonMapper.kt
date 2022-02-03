package com.tokopedia.gm.common.domain.mapper

import com.tokopedia.gm.common.utils.GoldMerchantUtil
import com.tokopedia.gm.common.data.source.cloud.model.ShopInfoPeriodWrapperResponse
import com.tokopedia.gm.common.presentation.model.ShopInfoPeriodUiModel
import javax.inject.Inject

open class ShopScoreCommonMapper @Inject constructor() {

    fun mapToGetShopInfo(shopInfoPeriodWrapperResponse: ShopInfoPeriodWrapperResponse): ShopInfoPeriodUiModel {
        val dateShopCreated = shopInfoPeriodWrapperResponse.shopInfoByIDResponse?.result
            ?.firstOrNull()?.createInfo?.shopCreated.orEmpty()
        return ShopInfoPeriodUiModel(
            periodType = shopInfoPeriodWrapperResponse.goldGetPMSettingInfo?.periodType ?: "",
            isNewSeller = GoldMerchantUtil.isNewSeller(
                dateShopCreated
            ),
            shopAge = GoldMerchantUtil.totalDays(
                dateShopCreated
            ),
            dateShopCreated = dateShopCreated,
            periodStartDate = shopInfoPeriodWrapperResponse.goldGetPMSettingInfo
                ?.periodStartDate.orEmpty(),
            periodEndDate = shopInfoPeriodWrapperResponse.goldGetPMSettingInfo
                ?.periodEndDate.orEmpty(),
        )
    }
}