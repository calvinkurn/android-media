package com.tokopedia.gm.common.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.gm.common.constant.PeriodType

/**
 * Created By @ilhamsuaib on 08/03/21
 */

data class GetPowerMerchantSettingInfoResponse(
    @Expose
    @SerializedName("goldGetPMSettingInfo")
    val goldGetPMSettingInfo: PMSettingInfoModel? = null
)

data class PMSettingInfoModel(
    @Expose
    @SerializedName("period_type")
    val periodeType: String? = PeriodType.COMMUNICATION_PERIOD,
    @Expose
    @SerializedName("period_start_date_time")
    val periodStartDateTime: String? = "",
    @Expose
    @SerializedName("ticker_list")
    val tickers: List<PmTickerModel>? = emptyList()
)