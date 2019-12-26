package com.tokopedia.play.view.uimodel


/**
 * Created by mzennis on 2019-12-26.
 */
data class EventUiModel(
        var isBanned: Boolean,
        var isFreeze: Boolean,
        var bannedMessage: String = "",
        var bannedTitle: String = "",
        var bannedButtonTitle: String = "",
        var bannedButtonUrl: String = "",
        var freezeMessage: String = "",
        var freezeTitle: String = "",
        var freezeButtonTitle: String = "",
        var freezeButtonUrl: String = ""
)