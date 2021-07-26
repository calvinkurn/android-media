package com.tokopedia.gm.common.domain.mapper

import com.tokopedia.gm.common.constant.GoldMerchantUtil
import com.tokopedia.gm.common.data.source.cloud.model.ShopInfoPeriodWrapperResponse
import com.tokopedia.gm.common.presentation.model.ShopInfoPeriodUiModel
import javax.inject.Inject

class ShopScoreCommonMapper @Inject constructor() {

    fun mapToGetShopInfo(shopInfoPeriodWrapperResponse: ShopInfoPeriodWrapperResponse): ShopInfoPeriodUiModel {
        return ShopInfoPeriodUiModel(
                periodType = shopInfoPeriodWrapperResponse.goldGetPMSettingInfo?.periodType ?: "",
                isNewSeller = GoldMerchantUtil.isNewSeller(shopInfoPeriodWrapperResponse.shopInfoByIDResponse?.result?.firstOrNull()?.createInfo?.shopCreated.orEmpty()),
                isEndTenureNewSeller = GoldMerchantUtil.isTenureNewSeller(shopInfoPeriodWrapperResponse.shopInfoByIDResponse?.result?.firstOrNull()
                        ?.createInfo?.shopCreated.orEmpty()),
                shopAge = GoldMerchantUtil.totalDays(shopInfoPeriodWrapperResponse.shopInfoByIDResponse?.result?.firstOrNull()?.createInfo?.shopCreated.orEmpty()),
                periodStartDate = shopInfoPeriodWrapperResponse.goldGetPMSettingInfo?.periodStartDate.orEmpty(),
                periodEndDate = shopInfoPeriodWrapperResponse.goldGetPMSettingInfo?.periodEndDate.orEmpty()
        )
    }
}