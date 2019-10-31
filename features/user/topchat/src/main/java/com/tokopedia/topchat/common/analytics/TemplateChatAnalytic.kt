package com.tokopedia.topchat.common.analytics

import com.tokopedia.track.TrackApp
import javax.inject.Inject

class TemplateChatAnalytic @Inject constructor() {

    fun trackOnCheckedChange(value: Boolean) {
        val status = if (value) "on" else "off"
        TrackApp.getInstance().gtm.sendGeneralEvent(
                NAME_TEMPLATE_CHAT,
                CATEGORY_TEMPLATE_CHAT,
                ACTION_TOGGLE_TEMPLATE_CHAT,
                status
        )
    }

    fun trackAddTemplateChat() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                NAME_TEMPLATE_CHAT,
                CATEGORY_TEMPLATE_CHAT,
                ACTION_CLICK_ADD_TEMPLATE_CHAT,
                ""
        )
    }

    companion object {
        const val NAME_TEMPLATE_CHAT = "clickTemplateChat"

        const val CATEGORY_TEMPLATE_CHAT = "template chat"

        const val ACTION_TOGGLE_TEMPLATE_CHAT = "click on toggle setting"
        const val ACTION_CLICK_ADD_TEMPLATE_CHAT = "click on add template chat"
    }
}