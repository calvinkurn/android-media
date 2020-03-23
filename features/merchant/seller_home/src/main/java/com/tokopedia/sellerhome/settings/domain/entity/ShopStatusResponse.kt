package com.tokopedia.sellerhome.settings.domain.entity

import com.google.gson.annotations.SerializedName
import com.tokopedia.sellerhome.settings.view.uimodel.base.PowerMerchantStatus
import com.tokopedia.sellerhome.settings.view.uimodel.base.RegularMerchant
import com.tokopedia.sellerhome.settings.view.uimodel.base.ShopType

data class ShopStatusResponse (
        @SerializedName("goldGetPMOSStatus")
        val shopStatusResult: ShopStatusResult? = ShopStatusResult())

data class ShopStatusResult(
        @SerializedName("data")
        val shopStatus: ShopStatus? = ShopStatus())

data class ShopStatus(
        @SerializedName("power_merchant")
        val powerMerchantStatus: PowerMerchant? = PowerMerchant(),
        @SerializedName("official_store")
        val officialStoreStatus: OfficialStore? = OfficialStore()) {

    companion object {
        const val STATUS_ACTIVE = "active"
        const val STATUS_INACTIVE = "inactive"
        const val STATUS_IDLE = "idle"
        const val STATUS_OFF = "off"
        const val STATUS_ON = "on"
        const val STATUS_PENDING = "pending"
    }

    fun getShopType(): ShopType {
        return if (officialStoreStatus?.status == STATUS_ACTIVE) {
            ShopType.OfficialStore
        } else {
            when(powerMerchantStatus?.status) {
                STATUS_INACTIVE -> RegularMerchant.NeedUpgrade
                STATUS_IDLE -> PowerMerchantStatus.NotActive
                STATUS_PENDING -> PowerMerchantStatus.OnVerification
                STATUS_ON -> PowerMerchantStatus.Active
                else -> RegularMerchant.NeedVerification
            }
        }
    }
}

data class PowerMerchant(
        @SerializedName("status")
        val status: String? = "")

data class OfficialStore(
        @SerializedName("status")
        val status: String? = "",
        @SerializedName("error")
        val osError: String? = "")
