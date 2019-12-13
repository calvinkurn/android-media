package com.tokopedia.topchat.chatsetting.analytic

import com.tokopedia.topchat.chatsetting.data.ChatSetting
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import javax.inject.Inject

class ChatSettingAnalytic @Inject constructor() {

    object Event {
        const val CLICK_INBOX_CHAT = "clickInboxChat"
    }

    object Category {
        const val CATEGORY_INBOX_CHAT = "inbox-chat"
    }

    object Action {
        const val CLICK_CHAT_SETTING = "click on gear icon setting"
    }

    // #CL4
    fun eventClickChatSetting(element: ChatSetting) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                TrackAppUtils.gtmData(
                        Event.CLICK_INBOX_CHAT,
                        Category.CATEGORY_INBOX_CHAT,
                        Action.CLICK_CHAT_SETTING,
                        element.alias
                )
        )
    }

}