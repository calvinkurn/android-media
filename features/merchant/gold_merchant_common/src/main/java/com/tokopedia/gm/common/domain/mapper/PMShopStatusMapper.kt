package com.tokopedia.gm.common.domain.mapper

import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.gm.common.constant.PMConstant
import com.tokopedia.gm.common.constant.PMStatusConst
import com.tokopedia.gm.common.data.source.cloud.model.ShopStatusModel
import com.tokopedia.gm.common.data.source.local.model.PMStatusUiModel
import com.tokopedia.kotlin.extensions.view.orZero
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 16/03/21
 */

class PMShopStatusMapper @Inject constructor() {

    fun mapRemoteModelToUiModel(shopStatus: ShopStatusModel): PMStatusUiModel {
        val statusOff = PMStatusUiModel.PM_AUTO_EXTEND_OFF
        return PMStatusUiModel(
                status = shopStatus.powerMerchant?.status ?: PMStatusConst.INACTIVE,
                pmTier = shopStatus.powerMerchant?.pmTire ?: PMConstant.PMTierType.POWER_MERCHANT,
                expiredTime = getExpiredTimeFmt(shopStatus.powerMerchant?.expiredTime.orEmpty()),
                autoExtendEnabled = shopStatus.powerMerchant?.autoExtend?.status != statusOff,
                isOfficialStore = shopStatus.officialStore?.status == PMStatusConst.ACTIVE,
                subscriptionType = shopStatus.powerMerchant?.autoExtend?.tkpdProductId.orZero()
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