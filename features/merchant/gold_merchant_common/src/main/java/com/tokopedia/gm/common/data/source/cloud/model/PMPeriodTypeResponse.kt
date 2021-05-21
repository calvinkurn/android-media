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
            @SerializedName("period_type")
            val periodType: String = "",
            @Expose
            @SerializedName("period_start_date_time")
            val periodStartDate: String? = "",
            @Expose
            @SerializedName("period_end_date_time")
            val periodEndDate: String? = "",
    )
}