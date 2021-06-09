package com.tokopedia.gm.common.data.source.local.model

/**
 * Created By @ilhamsuaib on 08/03/21
 */

data class TickerUiModel(
        val title: String = "",
        val text: String = "",
        val type: String = TYPE_INFO,
        val isInterruptPopup: Boolean = false
) {
    companion object {
        const val TYPE_INFO = "INFO"
        const val TYPE_WARNING = "WARNING"
        const val TYPE_ERROR = "ERROR"
    }
}