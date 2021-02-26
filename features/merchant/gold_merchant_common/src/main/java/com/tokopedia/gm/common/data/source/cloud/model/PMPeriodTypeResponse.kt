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
            val `data`: Data = Data()
    ) {
        data class Data(
                @Expose
                @SerializedName("period_type")
                val periodType: String = ""
        )
    }
}