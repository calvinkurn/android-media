package com.tokopedia.topchat.chatsetting.data.uimodel

import androidx.annotation.Keep

@Keep
class ChatSettingUtilsUiModel(
        alias: String = "",
        description: String = "",
        label: String = "",
        link: String = "",
        typeLabel: Int = 0
) : ItemChatSettingUiModel(
        alias,
        description,
        label,
        link,
        typeLabel
)