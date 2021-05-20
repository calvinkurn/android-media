package com.tokopedia.gm.common.data.source.local.model

/**
 * Created By @ilhamsuaib on 16/03/21
 */

data class PMStatusUiModel(
        val status: String = "",
        val expiredTime: String = "",
        val isOfficialStore: Boolean = false,
        val autoExtendEnabled: Boolean = true
) {
    companion object {
        const val AUTO_EXTEND_STATUS = "off"
    }
}