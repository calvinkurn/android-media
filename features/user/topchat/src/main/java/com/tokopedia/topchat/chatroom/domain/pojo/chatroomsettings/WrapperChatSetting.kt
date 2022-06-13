package com.tokopedia.topchat.chatroom.domain.pojo.chatroomsettings

import com.tokopedia.topchat.chatroom.view.viewmodel.BroadcastSpamHandlerUiModel
import com.tokopedia.usecase.coroutines.Result

data class WrapperChatSetting(
    var blockActionType: ActionType,
    var response: Result<ChatSettingsResponse>,
    var element: BroadcastSpamHandlerUiModel? = null
)

enum class BlockActionType(val value: String): ActionType {
    BlockChat("1"),
    UnblockChat("2"),
    BlockPromo("3"),
    UnblockPromo("4")
}

interface ActionType