package com.tokopedia.shop.score.performance.domain.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GoldGetPMOStatusResponse(
    @Expose
    @SerializedName("goldGetPMOSStatus")
    val goldGetPMOSStatus: GoldGetPMOSStatus = GoldGetPMOSStatus()
) {
    data class GoldGetPMOSStatus(
        @Expose
        @SerializedName("data")
        val `data`: Data = Data()
    ) {
        data class Data(
            @Expose
            @SerializedName("power_merchant")
            val powerMerchant: PowerMerchant = PowerMerchant(),
            @Expose
            @SerializedName("official_store")
            val officialStore: OfficialStore = OfficialStore()
        ) {
            data class PowerMerchant(
                @Expose
                @SerializedName("pm_tier")
                val pmTier: Int = 0,
                @Expose
                @SerializedName("status")
                val status: String = ""
            )

            data class OfficialStore(
                @Expose
                @SerializedName("status")
                val status: String = "",
                @Expose
                @SerializedName("error")
                val error: String = ""
            )
        }
    }
}