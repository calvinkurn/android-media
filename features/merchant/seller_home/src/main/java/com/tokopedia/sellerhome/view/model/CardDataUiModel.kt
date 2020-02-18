package com.tokopedia.sellerhome.view.model

/**
 * Created By @ilhamsuaib on 2020-01-28
 */

data class CardDataUiModel(
        override val dataKey: String = "",
        val description: String = "",
        val state: String = "",
        val value: String = "",
        override var error: String = ""
): BaseDataUiModel