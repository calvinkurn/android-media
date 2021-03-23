package com.tokopedia.gm.common.data.source.cloud.model

data class ShopInfoPeriodWrapperResponse(
        var shopInfoByIDResponse: ShopInfoByIDResponse.ShopInfoByID? = null,
        var goldGetPMSettingInfo: PMPeriodTypeResponse.GoldGetPMSettingInfo? = null
)