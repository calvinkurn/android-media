package com.tokopedia.gm.common.data.source.local.model

import com.tokopedia.gm.common.constant.PMConstant

/**
 * Created By @ilhamsuaib on 16/03/21
 */

data class PMStatusUiModel(
        val status: String = "",
        val pmTier: Int = PMConstant.PMTierType.NA,
        val expiredTime: String = "",
        val isOfficialStore: Boolean = false,
        val autoExtendEnabled: Boolean = true
) {
    companion object {
        const val PM_AUTO_EXTEND_OFF = "off"
    }
}