package com.tokopedia.sellerhomecommon.presentation.model

/**
 * Created By @ilhamsuaib on 09/11/20
 */

data class AnnouncementDataUiModel(
        override val dataKey: String = "",
        override var error: String = "",
        override var isFromCache: Boolean = false,
        val subtitle: String = "",
        val title: String = "",
        val appLink: String = "",
        val imgUrl: String = ""
) : BaseDataUiModel