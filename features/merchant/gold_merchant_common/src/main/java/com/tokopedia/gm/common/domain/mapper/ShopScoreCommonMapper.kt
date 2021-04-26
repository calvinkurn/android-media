package com.tokopedia.gm.common.domain.mapper

import com.tokopedia.gm.common.constant.GoldMerchantUtil.diffDays
import com.tokopedia.gm.common.constant.GoldMerchantUtil.isTenureNewSeller
import com.tokopedia.gm.common.constant.GoldMerchantUtil.totalDays
import com.tokopedia.gm.common.constant.NEW_SELLER_DAYS
import com.tokopedia.gm.common.data.source.cloud.model.ShopInfoPeriodWrapperResponse
import com.tokopedia.gm.common.presentation.model.ShopInfoPeriodUiModel
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class ShopScoreCommonMapper @Inject constructor() {

    fun mapToGetShopInfo(shopInfoPeriodWrapperResponse: ShopInfoPeriodWrapperResponse): ShopInfoPeriodUiModel {
        return ShopInfoPeriodUiModel(
                periodType = shopInfoPeriodWrapperResponse.goldGetPMSettingInfo?.periodType ?: "",
                isNewSeller = shopInfoPeriodWrapperResponse.shopInfoByIDResponse?.result?.firstOrNull()?.createInfo?.shopCreated.orEmpty()
                        isNewSeller (NEW_SELLER_DAYS),
                isEndTenureNewSeller = shopInfoPeriodWrapperResponse.shopInfoByIDResponse?.result?.firstOrNull()
                        ?.createInfo?.shopCreated.orEmpty().isTenureNewSeller(),
                shopAge = shopInfoPeriodWrapperResponse.shopInfoByIDResponse?.result?.firstOrNull()?.createInfo?.shopCreated.orEmpty().totalDays(),
                periodStartDate = shopInfoPeriodWrapperResponse.goldGetPMSettingInfo?.periodStartDate.orEmpty(),
                periodEndDate = shopInfoPeriodWrapperResponse.goldGetPMSettingInfo?.periodEndDate.orEmpty()
        )
    }

    private infix fun String.isNewSeller(days: Int) = diffDays(days)

}