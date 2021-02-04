package com.tokopedia.play.view.uimodel.recom

import com.tokopedia.play.view.uimodel.recom.types.PlayStatusType


/**
 * Created by mzennis on 04/02/21.
 */
data class PlayEventUiModel(
        val statusType: PlayStatusType,
        val bannedMessage: String = "",
        val bannedTitle: String = "",
        val bannedButtonTitle: String = "",
        val freezeTitle: String = "",
)