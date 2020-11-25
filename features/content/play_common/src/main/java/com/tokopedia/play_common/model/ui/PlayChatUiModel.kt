package com.tokopedia.play_common.model.ui

/**
 * Created by jegul on 09/06/20
 */
data class PlayChatUiModel(
        val messageId: String,
        val userId: String,
        val name: String,
        val message: String,
        val isSelfMessage: Boolean
)