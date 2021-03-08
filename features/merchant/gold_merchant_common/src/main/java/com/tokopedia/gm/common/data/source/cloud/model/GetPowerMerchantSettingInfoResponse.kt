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
        @SerializedName("shop_id")
        val shopId: Long? = 0,
        @SerializedName("is_new_pm_content")
        val isNewPmContent: Boolean? = false,
        @SerializedName("is_final_success_popup")
        val isFinalSuccessPopup: Boolean? = false,
        @SerializedName("period_type")
        val periodeType: String? = PeriodType.COMMUNICATION_PERIOD,
        @SerializedName("ticker_list")
        val tickers: List<PmTickerModel>? = emptyList()
)