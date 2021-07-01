package com.tokopedia.gm.common.data.source.local.model

import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.gm.common.constant.PMConstant
import com.tokopedia.gm.common.constant.PMStatusConst

/**
 * Created By @ilhamsuaib on 16/03/21
 */

data class PMStatusUiModel(
        val shopId: String = "",
        val status: String = PMStatusConst.INACTIVE,
        val pmTier: Int = PMConstant.PMTierType.NA,
        val expiredTime: String = "",
        val isOfficialStore: Boolean = false,
        val autoExtendEnabled: Boolean = true
) {
    companion object {
        const val PM_AUTO_EXTEND_OFF = "off"
    }

    fun getExpiredTimeFmt(newFormat: String): String {
        return try {
            val currentFormat = "dd MMMM yyyy HH:mm:ss"
            DateFormatUtils.formatDate(currentFormat, newFormat, expiredTime)
        } catch (e: IllegalArgumentException) {
            expiredTime
        }
    }

    fun isPowerMerchant(): Boolean = status == PMStatusConst.ACTIVE || status == PMStatusConst.IDLE
}