package com.tokopedia.shop.score.stub.performance.domain.response

import com.google.gson.annotations.SerializedName
import com.tokopedia.gm.common.data.source.cloud.model.PMPeriodTypeResponse
import com.tokopedia.gm.common.data.source.cloud.model.ShopInfoByIDResponse

data class ShopInfoPeriodResponseStub(
    @SerializedName("shopInfoByID")
    val shopInfoByIDResponse: ShopInfoByIDResponse.ShopInfoByID? = ShopInfoByIDResponse.ShopInfoByID(),
    @SerializedName("goldGetPMSettingInfo")
    val goldGetPMSettingInfo: PMPeriodTypeResponse.GoldGetPMSettingInfo? = PMPeriodTypeResponse.GoldGetPMSettingInfo()
)