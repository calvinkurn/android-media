package com.tokopedia.imagepicker_insta.common.ui.model

/**
 * Created By : Jonathan Darwin on April 13, 2022
 */
data class FeedAccountUiModel(
    val id: String,
    val name: String,
    val iconUrl: String,
    val badge: String,
) {
    companion object {
        val Empty = FeedAccountUiModel(
            id = "",
            name = "",
            iconUrl = "",
            badge = "",
        )
    }
}