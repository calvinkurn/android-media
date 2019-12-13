package com.tokopedia.topchat.chatsearch.analytic

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import javax.inject.Inject

class ChatSearchAnalytic @Inject constructor() {

    object Event {
        const val CLICK_INBOX_CHAT = "clickInboxChat"
    }

    object Category {
        const val CATEGORY_INBOX_CHAT = "inbox-chat"
    }

    object Action {
        const val SEARCH_CHAT = "search on chatlist"
    }

    // #CL1
    fun eventQueryTriggered() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                TrackAppUtils.gtmData(
                        Event.CLICK_INBOX_CHAT,
                        Category.CATEGORY_INBOX_CHAT,
                        Action.SEARCH_CHAT,
                        ""
                )
        )
    }
}