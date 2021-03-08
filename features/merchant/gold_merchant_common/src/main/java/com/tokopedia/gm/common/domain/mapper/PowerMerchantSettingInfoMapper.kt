package com.tokopedia.gm.common.domain.mapper

import com.tokopedia.gm.common.constant.PeriodType
import com.tokopedia.gm.common.data.source.cloud.model.PMSettingInfoModel
import com.tokopedia.gm.common.data.source.local.model.PowerMerchantSettingInfoUiModel
import com.tokopedia.gm.common.data.source.local.model.TickerUiModel
import com.tokopedia.kotlin.extensions.view.orZero
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 08/03/21
 */

class PowerMerchantSettingInfoMapper @Inject constructor() {

    fun mapRemoteModelToUiModel(response: PMSettingInfoModel?): PowerMerchantSettingInfoUiModel {
        return PowerMerchantSettingInfoUiModel(
                shopId = response?.shopId.orZero().toString(),
                isNewPmContent = response?.isNewPmContent ?: false,
                isFinalSuccessPopup = response?.isFinalSuccessPopup ?: false,
                periodeType = response?.periodeType ?: PeriodType.COMMUNICATION_PERIOD,
                tickers = response?.tickers?.map {
                    TickerUiModel(
                            title = it.title.orEmpty(),
                            text = it.text.orEmpty(),
                            type = it.type ?: TickerUiModel.TYPE_INFO
                    )
                }.orEmpty()
        )
    }
}