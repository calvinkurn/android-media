package com.tokopedia.sellerhomecommon.presentation.model

/**
 * Created By @ilhamsuaib on 19/05/20
 */

interface BaseDataUiModel {
    val dataKey: String
    var error: String
    var isFromCache: Boolean
    val showWidget: Boolean

    fun shouldRemove(): Boolean
}