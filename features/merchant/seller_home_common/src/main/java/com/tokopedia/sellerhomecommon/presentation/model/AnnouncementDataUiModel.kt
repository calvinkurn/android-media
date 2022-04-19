package com.tokopedia.sellerhomecommon.presentation.model

/**
 * Created By @ilhamsuaib on 09/11/20
 */

data class AnnouncementDataUiModel(
    override var dataKey: String = "",
    override var error: String = "",
    override var isFromCache: Boolean = false,
    override val showWidget: Boolean = false,
    val subtitle: String = "",
    val title: String = "",
    val appLink: String = "",
    val imgUrl: String = ""
) : BaseDataUiModel {

    override fun isWidgetEmpty(): Boolean {
        return title.isBlank() || subtitle.isBlank()
    }
}