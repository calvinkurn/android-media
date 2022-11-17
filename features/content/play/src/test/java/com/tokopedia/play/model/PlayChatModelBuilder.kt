package com.tokopedia.play.model

import com.tokopedia.play.view.uimodel.PlayChatHistoryUiModel
import com.tokopedia.play_common.model.ui.PlayChatUiModel

/**
 * Created by jegul on 15/02/21
 */
class PlayChatModelBuilder {

    fun build(
            messageId: String = "",
            userId: String = "123",
            name: String = "John Doe",
            message: String = "Hello",
            isSelfMessage: Boolean = true
    ) = PlayChatUiModel(
            messageId = messageId,
            userId = userId,
            name = name,
            message = message,
            isSelfMessage = isSelfMessage
    )

    fun buildHistory(
        size: Int = 10,
        messageId: String = "",
        userId: String = "123",
        name: String = "John Doe",
        message: String = "Hello",
        isSelfMessage: Boolean = true,
        nextCursor: String = "",
    ) = PlayChatHistoryUiModel(
        chatList = List(size) {
            build(
                messageId = messageId,
                userId = userId,
                name = name,
                message = message + it,
                isSelfMessage = isSelfMessage,
            )
        },
        nextCursor = nextCursor,
    )
}
