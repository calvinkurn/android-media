package com.tokopedia.gm.common.data.source.cloud.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PMPeriodTypeResponse(
        @Expose
        @SerializedName("goldGetPMSettingInfo")
        val goldGetPMSettingInfo: GoldGetPMSettingInfo = GoldGetPMSettingInfo()
) {
    data class GoldGetPMSettingInfo(
            @Expose
            @SerializedName("data")
            val `data`: GoldPMSettingInfoData = GoldPMSettingInfoData()
    ) {
        data class GoldPMSettingInfoData(
                @Expose
                @SerializedName("period_type")
                val periodType: String = ""
        )
    }
}