package com.tokopedia.shop.score.performance.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GoldGetPMStatusResponse(
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
                val powerMerchant: PowerMerchant = PowerMerchant()
        ) {
            data class PowerMerchant(
                    @Expose
                    @SerializedName("status")
                    val status: String = ""
            )
        }
    }
}