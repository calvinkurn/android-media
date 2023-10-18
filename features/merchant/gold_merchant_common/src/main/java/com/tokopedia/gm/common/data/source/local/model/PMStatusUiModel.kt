package com.tokopedia.gm.common.data.source.local.model

import com.tokopedia.gm.common.constant.PMConstant
import com.tokopedia.gm.common.constant.PMStatusConst
import com.tokopedia.gm.common.constant.PMTier
import com.tokopedia.kotlin.extensions.view.ZERO

/**
 * Created By @ilhamsuaib on 16/03/21
 */

data class PMStatusUiModel(
    val shopId: String = "",
    val status: String = PMStatusConst.INACTIVE,
    val pmTier: Int = PMConstant.PMTierType.NA,
    val expiredTime: String = "",
    val isOfficialStore: Boolean = false,
    val autoExtendEnabled: Boolean = true,
    val subscriptionType: Int = Int.ZERO
) {
    companion object {
        const val PM_AUTO_EXTEND_OFF = "off"
    }

    fun isRegularMerchant(): Boolean =
        status == PMStatusConst.INACTIVE && pmTier == PMTier.REGULAR

    fun isPowerMerchant(): Boolean = status == PMStatusConst.ACTIVE || status == PMStatusConst.IDLE

    fun isPowerMerchantPro(): Boolean = status == PMStatusConst.ACTIVE && pmTier == PMTier.PRO

    fun isPowerMerchantIdle(): Boolean = status == PMStatusConst.IDLE
}
