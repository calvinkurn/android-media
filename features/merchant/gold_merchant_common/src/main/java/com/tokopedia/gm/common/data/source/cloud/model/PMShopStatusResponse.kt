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
        val powerMerchant: PMDataModel? = null,
        @SerializedName("official_store")
        val officialStore: OfficialStoreStatusModel? = null
)

data class PMDataModel(
        @SerializedName("status")
        val status: String? = "",
        @SerializedName("pm_tier")
        val pmTire: Int? = 0,
        @SerializedName("expired_time")
        val expiredTime: String? = "",
        @SerializedName("auto_extend")
        val autoExtend: AutoExtendModel? = null
)

data class AutoExtendModel(
        @SerializedName("status")
        val status: String? = "",
)

data class OfficialStoreStatusModel(
        @SerializedName("status")
        val status: String? = ""
)