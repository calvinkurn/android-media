package com.tokopedia.play.model

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
}