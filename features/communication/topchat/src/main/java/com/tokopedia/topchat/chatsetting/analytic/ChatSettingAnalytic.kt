package com.tokopedia.topchat.chatsetting.analytic

import com.tokopedia.topchat.chatsetting.data.uimodel.ItemChatSettingUiModel
import com.tokopedia.topchat.common.analytics.TopChatAnalytics
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import javax.inject.Inject

class ChatSettingAnalytic @Inject constructor() {

    object Action {
        const val CLICK_CHAT_SETTING = "click on gear icon setting"
    }

    // #CL4
    fun eventClickChatSetting(element: ItemChatSettingUiModel) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                TrackAppUtils.gtmData(
                        TopChatAnalytics.Name.INBOX_CHAT,
                        TopChatAnalytics.Category.INBOX_CHAT,
                        Action.CLICK_CHAT_SETTING,
                        element.alias
                )
        )
    }

}