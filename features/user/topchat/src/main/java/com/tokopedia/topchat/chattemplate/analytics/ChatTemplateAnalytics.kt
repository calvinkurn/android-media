package com.tokopedia.topchat.chattemplate.analytics

import com.tokopedia.topchat.common.analytics.TopChatAnalytics
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import javax.inject.Inject

/**
 * @author by nisie on 02/01/19.
 */
class ChatTemplateAnalytics @Inject constructor() {

    object Companion {
        const val SCREEN_TEMPLATE_CHAT_SETTING = "template setting"
        const val SCREEN_TEMPLATE_CHAT_SET = "template update"
    }

    fun eventClickTemplate() {
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(TrackAppUtils.gtmData(
                TopChatAnalytics.Name.INBOX_CHAT,
                TopChatAnalytics.Category.ADD_TEMPLATE,
                TopChatAnalytics.Action.UPDATE_TEMPLATE,
                ""
        )
    }
}
