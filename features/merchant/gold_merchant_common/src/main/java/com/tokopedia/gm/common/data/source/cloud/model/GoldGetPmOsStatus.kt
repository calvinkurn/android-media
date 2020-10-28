package com.tokopedia.gm.common.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class GoldGetPmOsStatus(
        @SerializedName("goldGetPMOSStatus")
        @Expose
        val result : ShopStatusData = ShopStatusData()
)

data class ShopStatusData(
        @SerializedName("header")
        @Expose
        val header : ShopStatusHeader = ShopStatusHeader(),

        @SerializedName("data")
        @Expose
        val data : ShopStatusModel = ShopStatusModel()
)

data class ShopStatusHeader(
        @SerializedName("process_time")
        @Expose
        val processTime : Float = 0F ,

        @SerializedName("messages")
        @Expose
        val message : ArrayList<String> = arrayListOf(),

        @SerializedName("reason")
        @Expose
        val reason : String = "",

        @SerializedName("error_code")
        @Expose
        val errorCode : String = ""
)

data class ShopStatusModel(
        @SerializedName("official_store")
        @Expose
        val officialStore: OfficialStore = OfficialStore(),
        @SerializedName("power_merchant")
        @Expose
        val powerMerchant: PowerMerchant = PowerMerchant(),
        @SerializedName("shopID")
        @Expose
        val shopId: Int = 0
) {
    companion object {
        const val STATUS_ACTIVE = "active"
        const val STATUS_INACTIVE = "inactive"
        const val STATUS_IDLE = "idle"
        const val STATUS_ON = "on"
        const val STATUS_PENDING = "pending"
    }

    fun isPowerMerchantActive(): Boolean {
        return powerMerchant.status == STATUS_ACTIVE
    }

    fun isPowerMerchantIdle(): Boolean {
        return powerMerchant.status == STATUS_IDLE
    }

    fun isPowerMerchantPending(): Boolean {
        return powerMerchant.status == STATUS_PENDING
    }

    fun isPowerMerchantInactive(): Boolean {
        return powerMerchant.status == STATUS_INACTIVE
    }

    fun isPowerMerchantRegistered(): Boolean {
        return (isPowerMerchantActive() || isPowerMerchantIdle()) && isAutoExtend()
    }

    @Deprecated("prefer use isRegularMerchantOrPending")
    fun isRegularMerchant(): Boolean {
        return powerMerchant.status == STATUS_INACTIVE && officialStore.status == STATUS_INACTIVE
    }

    fun isRegularMerchantOrPending(): Boolean {
        return isRegularMerchant() || isPowerMerchantPending()
    }

    fun isOfficialStore(): Boolean {
        return officialStore.status == STATUS_ACTIVE
    }

    fun isAutoExtend(): Boolean {
        return powerMerchant.autoExtend.status == STATUS_ON
    }
}

