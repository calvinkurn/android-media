package com.tokopedia.sellerhome.settings.view.uimodel.base.partialresponse

import com.tokopedia.sellerhome.settings.domain.entity.OthersBalance
import com.tokopedia.sellerhome.settings.view.uimodel.base.ShopType

sealed class PartialSettingSuccessInfoType: PartialSettingResponse {

    data class PartialTopAdsSettingSuccessInfo(
            val topAdsBalance: Float,
            val isTopAdsAutoTopup: Boolean
    ) : PartialSettingSuccessInfoType()

    data class PartialShopSettingSuccessInfo(
            val othersBalance: OthersBalance,
            val shopStatusType: ShopType,
            val totalFollowers: Int,
            val shopBadgeUrl: String
    ): PartialSettingSuccessInfoType()

}