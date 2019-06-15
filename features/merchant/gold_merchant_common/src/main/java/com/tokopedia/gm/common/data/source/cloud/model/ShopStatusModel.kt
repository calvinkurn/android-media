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
) {
    companion object {
        const val STATUS_ACTIVE = "active"
        const val STATUS_INACTIVE = "inactive"
        const val STATUS_IDLE = "idle"
        const val STATUS_OFF = "off"
        const val STATUS_ON = "on"
    }

    fun isPowerMerchantActive(): Boolean {
        return powerMerchant.status == STATUS_ACTIVE
    }

    fun isPowerMerchantIdle(): Boolean {
        return powerMerchant.status == STATUS_IDLE
    }

    fun isPowerMerchantInactive(): Boolean {
        return powerMerchant.status == STATUS_INACTIVE
    }

    fun isRegularMerchant(): Boolean {
        return powerMerchant.status == STATUS_INACTIVE && officialStore.status == STATUS_INACTIVE
    }

    fun isTransitionPeriod(): Boolean {
        return powerMerchant.shopPopup
    }

    fun isAutoExtend(): Boolean {
        return powerMerchant.autoExtend.status == STATUS_OFF
    }
}