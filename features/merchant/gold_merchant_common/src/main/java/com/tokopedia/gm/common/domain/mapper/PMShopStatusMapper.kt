package com.tokopedia.gm.common.domain.mapper

import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.gm.common.constant.PMConstant
import com.tokopedia.gm.common.constant.PMStatusConst
import com.tokopedia.gm.common.data.source.cloud.model.PMShopStatusDataModel
import com.tokopedia.gm.common.data.source.local.model.PMStatusUiModel
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 16/03/21
 */

class PMShopStatusMapper @Inject constructor() {

    fun mapRemoteModelToUiModel(shopStatus: PMShopStatusDataModel): PMStatusUiModel {
        val statusOff = PMStatusUiModel.AUTO_EXTEND_STATUS
        return PMStatusUiModel(
                status = shopStatus.powerMerchant?.status.orEmpty(),
                pmTier = shopStatus.powerMerchant?.pmTire ?: PMConstant.PMTierType.POWER_MERCHANT,
                expiredTime = getExpiredTimeFmt(shopStatus.powerMerchant?.expiredTime.orEmpty()),
                autoExtendEnabled = shopStatus.powerMerchant?.autoExtend?.status != statusOff,
                isOfficialStore = shopStatus.officialStore?.status == PMStatusConst.ACTIVE
        )
    }

    private fun getExpiredTimeFmt(dateStr: String): String {
        return try {
            val currentFormat = "yyyy-MM-dd HH:mm:ss"
            val newFormat = "dd MMMM yyyy HH:mm:ss"
            DateFormatUtils.formatDate(currentFormat, newFormat, dateStr)
        } catch (e: IllegalArgumentException) {
            dateStr
        }
    }
}