package com.tokopedia.sellerhomecommon.presentation.model

/**
 * Created By @ilhamsuaib on 19/05/20
 */

data class CardDataUiModel(
        override val dataKey: String = "",
        val description: String = "",
        val state: String = "",
        val value: String = "",
        override var error: String = ""
): BaseDataUiModel