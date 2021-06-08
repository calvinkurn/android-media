package com.tokopedia.gm.common.data.source.cloud.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.gm.common.constant.PeriodType

/**
 * Created By @ilhamsuaib on 08/03/21
 */

data class GetPowerMerchantSettingInfoResponse(
        @SerializedName("goldGetPMSettingInfo")
        val goldGetPMSettingInfo: PMSettingInfoModel? = null
)

data class PMSettingInfoModel(
        @SerializedName("period_type")
        val periodeType: String? = PeriodType.COMMUNICATION_PERIOD,
        @SerializedName("period_type_pm_pro")
        val periodeTypePmPro: String? = PeriodType.COMMUNICATION_PERIOD_PM_PRO,
        @SerializedName("ticker_list")
        val tickers: List<PmTickerModel>? = emptyList()
)