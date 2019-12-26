package com.tokopedia.topchat.chatsearch.analytic

import com.tokopedia.topchat.common.analytics.TopChatAnalytics
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import javax.inject.Inject

class ChatSearchAnalytic @Inject constructor() {

    object Action {
        const val SEARCH_CHAT = "search on chatlist"
    }

    // #CL1
    fun eventQueryTriggered() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                TrackAppUtils.gtmData(
                        TopChatAnalytics.Name.INBOX_CHAT,
                        TopChatAnalytics.Category.INBOX_CHAT,
                        Action.SEARCH_CHAT,
                        ""
                )
        )
    }
}