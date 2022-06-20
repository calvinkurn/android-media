package com.tokopedia.imagepicker_insta.common.ui.model

/**
 * Created By : Jonathan Darwin on April 13, 2022
 */
data class FeedAccountUiModel(
    val id: String,
    val name: String,
    val iconUrl: String,
    val badge: String,
    val type: String,
    val hasUsername: Boolean,
    val hasAcceptTnc: Boolean,
) {
    val isUser: Boolean
        get() = type == TYPE_USER

    val isShop: Boolean
        get() = type == TYPE_SHOP

    val isUserPostEligible: Boolean
        get() = isUser && hasAcceptTnc

    companion object {
        private const val TYPE_USER = "content-user"
        private const val TYPE_SHOP = "content-shop"

        val Empty = FeedAccountUiModel(
            id = "",
            name = "",
            iconUrl = "",
            badge = "",
            type = "",
            hasUsername = false,
            hasAcceptTnc = false,
        )
    }
}