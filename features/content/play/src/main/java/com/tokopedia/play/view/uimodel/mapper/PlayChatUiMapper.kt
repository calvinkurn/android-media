package com.tokopedia.play.view.uimodel.mapper

import com.tokopedia.play.ui.chatlist.model.PlayChat
import com.tokopedia.play_common.model.ui.PlayChatUiModel
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by jegul on 01/02/21
 */
class PlayChatUiMapper @Inject constructor(
        private val userSession: UserSessionInterface
) {

    private val userId: String
        get() = userSession.userId

    fun mapChat(playChat: PlayChat) = PlayChatUiModel(
            messageId = playChat.messageId,
            userId = playChat.user.id,
            name = playChat.user.name,
            message = playChat.message,
            isSelfMessage = playChat.user.id == userId
    )
}