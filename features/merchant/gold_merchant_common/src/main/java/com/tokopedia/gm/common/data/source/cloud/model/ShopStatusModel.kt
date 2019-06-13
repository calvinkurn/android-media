package com.tokopedia.gm.common.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopStatusModel(
        @SerializedName("official_store")
        @Expose
        val officialStore: OfficialStore = OfficialStore(),
        @SerializedName("power_merchant")
        @Expose
        val powerMerchant: PowerMerchant = PowerMerchant(),
        @SerializedName("shop_id")
        @Expose
        val shopId: Int = 0
)