package com.tokopedia.topchat.chattemplate.analytics

import com.tokopedia.topchat.common.analytics.TopChatAnalytics
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import javax.inject.Inject

/**
 * @author by nisie on 02/01/19.
 */
class ChatTemplateAnalytics @Inject constructor() {

    object Companions {
        const val SCREEN_TEMPLATE_CHAT_SETTING = "template setting"
        const val SCREEN_TEMPLATE_CHAT_SET = "template update"
    }

    fun eventClickTemplate() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                TopChatAnalytics.Name.INBOX_CHAT,
                TopChatAnalytics.Category.ADD_TEMPLATE,
                TopChatAnalytics.Action.UPDATE_TEMPLATE,
                ""
        ))
    }


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

    fun trackEditTemplateChat() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                NAME_TEMPLATE_CHAT,
                CATEGORY_TEMPLATE_CHAT,
                ACTION_CLICK_ADD_TEMPLATE_CHAT,
                ""
        )
    }

    fun trackSaveTemplateChat() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                NAME_TEMPLATE_CHAT,
                CATEGORY_TEMPLATE_CHAT,
                ACTION_CLICK_SAVE_TEMPLATE_CHAT,
                ""
        )
    }

    fun trackDeleteTemplateChat() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                NAME_TEMPLATE_CHAT,
                CATEGORY_TEMPLATE_CHAT,
                ACTION_CLICK_DELETE_TEMPLATE_CHAT,
                ""
        )
    }

    fun trackConfirmDeleteTemplateChat() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                NAME_TEMPLATE_CHAT,
                CATEGORY_TEMPLATE_CHAT,
                ACTION_CLICK_CONFIRM_DELETE_TEMPLATE_CHAT,
                ""
        )
    }

    companion object {
        const val NAME_TEMPLATE_CHAT = "clickTemplateChat"

        const val CATEGORY_TEMPLATE_CHAT = "template chat"

        const val ACTION_TOGGLE_TEMPLATE_CHAT = "click on toggle setting"
        const val ACTION_CLICK_ADD_TEMPLATE_CHAT = "click on add template chat"
        const val ACTION_CLICK_SAVE_TEMPLATE_CHAT = "click on save edit template"
        const val ACTION_CLICK_DELETE_TEMPLATE_CHAT = "click on delete icon template chat"
        const val ACTION_CLICK_CONFIRM_DELETE_TEMPLATE_CHAT = "click yes on delete template chat"
    }
}
