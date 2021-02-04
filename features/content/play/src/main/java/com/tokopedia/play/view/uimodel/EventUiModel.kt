package com.tokopedia.play.view.uimodel


/**
 * Created by mzennis on 2019-12-26.
 */
data class EventUiModel(
        val isBanned: Boolean,
        val isFreeze: Boolean,
        val bannedMessage: String = "",
        val bannedTitle: String = "",
        val bannedButtonTitle: String = "",
        val freezeTitle: String = "",
)