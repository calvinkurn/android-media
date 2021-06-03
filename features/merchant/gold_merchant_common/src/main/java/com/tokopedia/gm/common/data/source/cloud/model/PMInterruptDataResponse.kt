package com.tokopedia.gm.common.data.source.cloud.model

import com.google.gson.annotations.SerializedName

/**
 * Created By @ilhamsuaib on 20/03/21
 */

data class PMInterruptDataResponse(
        @SerializedName("goldGetPMShopInfo")
        val shopInfo: GoldGetPMShopInfoDataModel? = null,
        @SerializedName("goldGetPMOSStatus")
        val pmStatus: PMShopStatusModel? = null,
        @SerializedName("goldGetPMSettingInfo")
        val pmSettingInfo: PMSettingInfoModel? = null
)