package com.tokopedia.gm.common.data.source.cloud.model

import android.annotation.SuppressLint

@SuppressLint("ResponseFieldAnnotation")
data class ShopInfoPeriodWrapperResponse(
    var shopInfoByIDResponse: ShopInfoByIDResponse.ShopInfoByID? = null,
    var goldGetPMSettingInfo: PMPeriodTypeResponse.GoldGetPMSettingInfo? = null
)