package com.tokopedia.play.view.uimodel

/**
 * Created by jegul on 09/01/20
 */
data class PlayChatUiModel(
        val messageId: String,
        val userId: String,
        val name: String,
        val message: String,
        val isSelfMessage: Boolean
)