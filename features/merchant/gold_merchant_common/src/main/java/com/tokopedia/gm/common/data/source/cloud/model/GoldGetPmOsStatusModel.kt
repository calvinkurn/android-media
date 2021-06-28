package com.tokopedia.gm.common.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class GoldGetPmOsStatusModel(
        @SerializedName("goldGetPMOSStatus")
        @Expose
        val result: ShopStatusDataModel? = ShopStatusDataModel()
)

data class ShopStatusDataModel(
        @SerializedName("header")
        @Expose
        val header: ShopStatusHeaderModel? = ShopStatusHeaderModel(),

        @SerializedName("data")
        @Expose
        val data: ShopStatusModel? = ShopStatusModel()
)

data class ShopStatusHeaderModel(
        @SerializedName("messages")
        @Expose
        val message: List<String> = listOf(),
        @SerializedName("reason")
        @Expose
        val reason: String = "",
        @SerializedName("error_code")
        @Expose
        val errorCode: String = ""
)

data class OfficialStoreModel(
        @SerializedName("error")
        @Expose
        val error: String = "",
        @SerializedName("status")
        @Expose
        val status: String = ""
)

data class PowerMerchantModel(
        @SerializedName("auto_extend")
        @Expose
        val autoExtend: AutoExtend = AutoExtend(),
        @SerializedName("expired_time")
        @Expose
        val expiredTime: String = "",
        @SerializedName("pm_tier")
        @Expose
        val pmTire: Int? = 0,
        @SerializedName("status")
        @Expose
        val status: String = ""
)

data class ShopStatusModel(
        @SerializedName("official_store")
        @Expose
        val officialStore: OfficialStoreModel? = OfficialStoreModel(),
        @SerializedName("power_merchant")
        @Expose
        val powerMerchant: PowerMerchantModel? = PowerMerchantModel(),
        @SerializedName("shopID")
        @Expose
        val shopId: Long? = 0
)

