package com.tokopedia.seller.menu.common.view.uimodel.base.partialresponse

import com.tokopedia.seller.menu.common.domain.entity.OthersBalance
import com.tokopedia.seller.menu.common.view.uimodel.base.ShopType

sealed class PartialSettingSuccessInfoType: PartialSettingResponse {

    data class PartialTopAdsSettingSuccessInfo(
            val othersBalance: OthersBalance,
            val topAdsBalance: Float,
            val isTopAdsAutoTopup: Boolean
    ) : PartialSettingSuccessInfoType()

    data class PartialShopSettingSuccessInfo(
            val shopStatusType: ShopType,
            val totalFollowers: Long,
            val shopBadgeUrl: String
    ): PartialSettingSuccessInfoType()

}