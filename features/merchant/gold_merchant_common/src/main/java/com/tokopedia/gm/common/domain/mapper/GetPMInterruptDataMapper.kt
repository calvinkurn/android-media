package com.tokopedia.gm.common.domain.mapper

import com.tokopedia.gm.common.constant.PMStatusConst
import com.tokopedia.gm.common.constant.PeriodType
import com.tokopedia.gm.common.data.source.cloud.model.PMInterruptDataResponse
import com.tokopedia.gm.common.view.model.PowerMerchantInterruptUiModel
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 21/03/21
 */

class GetPMInterruptDataMapper @Inject constructor() {

    fun mapRemoteModelToUiModel(data: PMInterruptDataResponse): PowerMerchantInterruptUiModel {
        return PowerMerchantInterruptUiModel(
                isNewSeller = data.shopInfo?.isNewSeller ?: true,
                shopAge = data.shopInfo?.shopAge ?: PowerMerchantInterruptUiModel.MIN_SHOP_AGE,
                pmStatus = data.pmStatus?.data?.powerMerchant?.status.orEmpty(),
                isOfficialStore = data.pmStatus?.data?.officialStore?.status == PMStatusConst.ACTIVE,
                periodType = data.pmSettingInfo?.periodeType ?: PeriodType.COMMUNICATION_PERIOD
        )
    }
}