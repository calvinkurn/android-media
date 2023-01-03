package com.tokopedia.play_common.model.ui

/**
 * @author by astidhiyaa on 09/11/22
 */
data class ArchivedUiModel(
    val title: String,
    val description: String,
    val btnTitle: String,
    val appLink: String,
) {
    companion object {
        val Empty: ArchivedUiModel
            get() = ArchivedUiModel(
                title = "",
                description = "",
                btnTitle = "",
                appLink = "",
            )
    }
}
