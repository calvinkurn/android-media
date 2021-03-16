package com.tokopedia.gm.common.data.source.cloud.model

import com.google.gson.annotations.SerializedName

/**
 * Created By @ilhamsuaib on 16/03/21
 */

data class PMShopStatusResponse(
        @SerializedName("goldGetPMOSStatus")
        val shopStatus: PMShopStatusModel? = null
)

data class PMShopStatusModel(
        @SerializedName("data")
        val data: PMShopStatusDataModel? = null
)

data class PMShopStatusDataModel(
        @SerializedName("power_merchant")
        val powerMerchant: PMDataModel? = null
)

data class PMDataModel(
        @SerializedName("status")
        val status: String? = "",
        @SerializedName("expired_time")
        val expiredTime: String? = ""
)