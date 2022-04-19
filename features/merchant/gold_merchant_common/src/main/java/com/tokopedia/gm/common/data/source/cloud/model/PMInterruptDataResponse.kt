package com.tokopedia.gm.common.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created By @ilhamsuaib on 20/03/21
 */

data class PMInterruptDataResponse(
        @SerializedName("goldGetPMShopInfo")
        @Expose
        val shopInfo: GoldGetPMShopInfoDataModel? = null,
        @SerializedName("goldGetPMOSStatus")
        @Expose
        val pmStatus: ShopStatusDataModel? = null,
        @SerializedName("goldGetPMSettingInfo")
        @Expose
        val pmSettingInfo: PMSettingInfoModel? = null
)