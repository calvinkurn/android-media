package com.tokopedia.topchat.chatroom.domain.pojo.chatroomsettings

import com.tokopedia.topchat.chatroom.view.viewmodel.BroadcastSpamHandlerUiModel
import com.tokopedia.usecase.coroutines.Result

data class WrapperChatSetting(
    var actionType: ActionType,
    var response: Result<ChatSettingsResponse>,
    var element: BroadcastSpamHandlerUiModel? = null
)

enum class ActionType(val value: String) {
    BlockChat("1"),
    UnblockChat("2"),
    BlockPromo("3"),
    UnblockPromo("4")
}