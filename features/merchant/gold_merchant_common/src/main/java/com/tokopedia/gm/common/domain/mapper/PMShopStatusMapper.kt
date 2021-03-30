package com.tokopedia.gm.common.domain.mapper

import com.tokopedia.gm.common.constant.PMStatusConst
import com.tokopedia.gm.common.data.source.cloud.model.PMShopStatusDataModel
import com.tokopedia.gm.common.data.source.local.model.PMStatusUiModel
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 16/03/21
 */

class PMShopStatusMapper @Inject constructor() {

    fun mapRemoteModelToUiModel(shopStatus: PMShopStatusDataModel): PMStatusUiModel {
        return PMStatusUiModel(
                status = shopStatus.powerMerchant?.status.orEmpty(),
                expiredTime = shopStatus.powerMerchant?.expiredTime.orEmpty()
        )
    }
}