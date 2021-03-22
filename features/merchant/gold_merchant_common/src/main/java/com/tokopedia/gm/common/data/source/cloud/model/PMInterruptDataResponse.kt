package com.tokopedia.gm.common.data.source.cloud.model

import com.google.gson.annotations.SerializedName

/**
 * Created By @ilhamsuaib on 20/03/21
 */

data class PMInterruptDataResponse(
        @SerializedName("goldGetPMGradeBenefitInfo")
        val gradeBenefitInfo: PMGradeBenefitInfoModel,
        @SerializedName("goldGetPMShopInfo")
        val shopInfo: GoldGetPMShopInfoDataModel,
        @SerializedName("goldGetPMOSStatus")
        val pmStatus: PMShopStatusModel
)