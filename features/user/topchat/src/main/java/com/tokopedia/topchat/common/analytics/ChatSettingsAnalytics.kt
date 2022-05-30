package com.tokopedia.topchat.common.analytics

import javax.inject.Inject
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.topchat.common.analytics.ChatSettingsAnalytics

class ChatSettingsAnalytics @Inject constructor() {
    fun sendTrackingEvent(eventCategory: String?, eventAction: String?, eventLabel: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            TrackAppUtils.gtmData(
                EVENT_NAME,
                eventCategory,
                eventAction,
                eventLabel
            )
        )
    }

    // #APC5
    fun sendTrackingUnblockPromotion(shopId: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            TrackAppUtils.gtmData(
                EVENT_CLICK_CHAT_SETTING,
                CHAT_SETTINGS_CATEGORY,
                CHAT_UNBLOCK_ACTION,
                String.format("%s - %s", shopId, CHAT_PROMOTION_LABEL)
            )
        )
    }

    // #APC3
    fun sendTrackingBlockPromotion(shopId: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            TrackAppUtils.gtmData(
                EVENT_CLICK_CHAT_SETTING,
                CHAT_SETTINGS_CATEGORY,
                CHAT_BLOCK_ACTION,
                String.format("%s - %s", shopId, CHAT_PROMOTION_LABEL)
            )
        )
    }

    // #APC4
    fun sendTrackingUnblockPersonal(shopId: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            TrackAppUtils.gtmData(
                EVENT_CLICK_CHAT_SETTING,
                CHAT_SETTINGS_CATEGORY,
                CHAT_UNBLOCK_ACTION,
                String.format("%s - %s", shopId, CHAT_PERSONAL_LABEL)
            )
        )
    }

    // #APC2
    fun sendTrackingBlockPersonal(shopId: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            TrackAppUtils.gtmData(
                EVENT_CLICK_CHAT_SETTING,
                CHAT_SETTINGS_CATEGORY,
                CHAT_BLOCK_ACTION,
                String.format("%s - %s", shopId, CHAT_PERSONAL_LABEL)
            )
        )
    }

    fun sendOpenChatSettingTacking() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            TrackAppUtils.gtmData(
                EVENT_NAME,
                CHAT_OPEN_CATEGORY,
                CHAT_SETTINGS_ACTION,
                ""
            )
        )
    }

    fun sendEnableChatSettingTracking() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            TrackAppUtils.gtmData(
                EVENT_NAME,
                CHAT_OPEN_CATEGORY,
                CHAT_ENABLE_TEXT_LINK_ACTION,
                CHAT_ENABLE_TEXT_LABEL
            )
        )
    }

    companion object {
        const val EVENT_NAME = "ClickChatDetail"
        const val EVENT_CLICK_CHAT_SETTING = "clickChatSetting"
        const val CHAT_OPEN_CATEGORY = "chat detail"
        const val CHAT_SETTINGS_ACTION = "click on atur penerimaan chat button"
        const val CHAT_SETTINGS_CATEGORY = "chat page setting"
        const val CHAT_BLOCK_ACTION = "chat toggle un-receive"
        const val CHAT_UNBLOCK_ACTION = "chat toggle receive"
        const val CHAT_PERSONAL_LABEL = "personal chat"
        const val CHAT_PROMOTION_LABEL = "promotion chat"
        const val CHAT_ENABLE_TEXT_LINK_ACTION = "click on text link (kembali menerima pesan)"
        const val CHAT_ENABLE_TEXT_LABEL = "kembali menerima pesan"
    }
}