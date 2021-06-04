package com.tokopedia.gm.common.domain.mapper

import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.gm.common.constant.PeriodType
import com.tokopedia.gm.common.data.source.cloud.model.PMSettingInfoModel
import com.tokopedia.gm.common.data.source.local.model.PowerMerchantSettingInfoUiModel
import com.tokopedia.gm.common.data.source.local.model.TickerUiModel
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.orZero
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 08/03/21
 */

class PowerMerchantSettingInfoMapper @Inject constructor() {

    companion object {
        private const val INTERRUPT_POPUP_KEY = "interrupt_popup"
    }

    fun mapRemoteModelToUiModel(response: PMSettingInfoModel?): PowerMerchantSettingInfoUiModel {
        return PowerMerchantSettingInfoUiModel(
                periodeType = response?.periodeType ?: PeriodType.TRANSITION_PERIOD,
                periodeTypePmPro = response?.periodeTypePmPro ?: PeriodType.COMMUNICATION_PERIOD_PM_PRO,
                tickers = response?.tickers?.map {
                    TickerUiModel(
                            title = it.title.orEmpty(),
                            text = it.text.orEmpty(),
                            type = it.type ?: TickerUiModel.TYPE_INFO,
                            isInterruptPopup = it.text?.contains(INTERRUPT_POPUP_KEY).orFalse()
                    )
                }.orEmpty()
        )
    }
}