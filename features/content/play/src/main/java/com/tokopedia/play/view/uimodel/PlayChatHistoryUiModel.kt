package com.tokopedia.play.view.uimodel

import com.tokopedia.play_common.model.ui.PlayChatUiModel

/**
 * Created By : Jonathan Darwin on October 20, 2022
 */
data class PlayChatHistoryUiModel(
    val chatList: List<PlayChatUiModel>,
    val nextCursor: String,
)
