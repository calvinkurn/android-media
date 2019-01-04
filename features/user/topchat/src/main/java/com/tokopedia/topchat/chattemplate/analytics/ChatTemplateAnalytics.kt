package com.tokopedia.topchat.chattemplate.analytics

import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker
import com.tokopedia.topchat.common.analytics.TopChatAnalytics
import javax.inject.Inject

/**
 * @author by nisie on 02/01/19.
 */
class ChatTemplateAnalytics @Inject constructor(private val tracker: AnalyticTracker) {

    object Companion {
        const val SCREEN_TEMPLATE_CHAT_SETTING = "template setting"
        const val SCREEN_TEMPLATE_CHAT_SET = "template update"
    }

    fun eventClickTemplate() {
        tracker.sendEventTracking(
                TopChatAnalytics.Name.INBOX_CHAT,
                TopChatAnalytics.Category.ADD_TEMPLATE,
                TopChatAnalytics.Action.UPDATE_TEMPLATE,
                ""
        )
    }
}
