package com.tokopedia.tokochat.analytics

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.interfaces.ContextAnalytics
import javax.inject.Inject

class TokoChatAnalytics @Inject constructor() {

    private val tracking: ContextAnalytics by lazy(LazyThreadSafetyMode.NONE) { TrackApp.getInstance().gtm }

    fun clickCallButtonFromChatRoom(
        orderStatus: String,
        orderId: String,
        channelId: String,
        source: String,
        role: String
    ) {
        val mapData = mapOf(
            TrackAppUtils.EVENT to TokoChatAnalyticsConstants.CLICK_COMMUNICATION,
            TrackAppUtils.EVENT_ACTION to TokoChatAnalyticsConstants.CLICK_CALL_BUTTON_FROM_CHATROOM,
            TrackAppUtils.EVENT_CATEGORY to TokoChatAnalyticsConstants.TOKOCHAT_DETAIL,
            TrackAppUtils.EVENT_LABEL to "$orderStatus - $orderId - $channelId - $source - $role",
            TokoChatAnalyticsConstants.TRACKER_ID to TokoChatAnalyticsConstants.TRACKER_ID_39069,
            TokoChatAnalyticsConstants.BUSSINESS_UNIT to TokoChatAnalyticsConstants.COMMUNICATION,
            TokoChatAnalyticsConstants.CURRENT_SITE to TokoChatAnalyticsConstants.TOKOPEDIA_MARKETPLACE,
        )
        tracking.sendGeneralEvent(mapData)
    }

    fun clickCloseOrderWidget(
        channelId: String,
        role: String,
        source: String,
    ) {
        val mapData = mapOf(
            TrackAppUtils.EVENT to TokoChatAnalyticsConstants.CLICK_COMMUNICATION,
            TrackAppUtils.EVENT_ACTION to TokoChatAnalyticsConstants.CLICK_CLOSE_ORDER_WIDGET,
            TrackAppUtils.EVENT_CATEGORY to TokoChatAnalyticsConstants.TOKOCHAT_DETAIL,
            TrackAppUtils.EVENT_LABEL to "$channelId - $role - $source",
            TokoChatAnalyticsConstants.TRACKER_ID to TokoChatAnalyticsConstants.TRACKER_ID_39071,
            TokoChatAnalyticsConstants.BUSSINESS_UNIT to TokoChatAnalyticsConstants.COMMUNICATION,
            TokoChatAnalyticsConstants.CURRENT_SITE to TokoChatAnalyticsConstants.TOKOPEDIA_MARKETPLACE,
        )
        tracking.sendGeneralEvent(mapData)
    }

    fun clickSendMessage(
        channelId: String,
        role: String,
        source: String,
    ) {
        val mapData = mapOf(
            TrackAppUtils.EVENT to TokoChatAnalyticsConstants.CLICK_COMMUNICATION,
            TrackAppUtils.EVENT_ACTION to TokoChatAnalyticsConstants.CLICK_SEND_MESSAGE,
            TrackAppUtils.EVENT_CATEGORY to TokoChatAnalyticsConstants.TOKOCHAT_DETAIL,
            TrackAppUtils.EVENT_LABEL to "$channelId - $role - $source",
            TokoChatAnalyticsConstants.TRACKER_ID to TokoChatAnalyticsConstants.TRACKER_ID_39072,
            TokoChatAnalyticsConstants.BUSSINESS_UNIT to TokoChatAnalyticsConstants.COMMUNICATION,
            TokoChatAnalyticsConstants.CURRENT_SITE to TokoChatAnalyticsConstants.TOKOPEDIA_MARKETPLACE,
        )
        tracking.sendGeneralEvent(mapData)
    }

    fun impressOnClosedChatroomTicker(
        channelId: String,
        role: String,
        source: String,
    ) {
        val mapData = mapOf(
            TrackAppUtils.EVENT to TokoChatAnalyticsConstants.VIEW_COMMUNICATION_IRIS,
            TrackAppUtils.EVENT_ACTION to TokoChatAnalyticsConstants.IMPRESSION_ON_CLOSED_CHATROM_TICKER,
            TrackAppUtils.EVENT_CATEGORY to TokoChatAnalyticsConstants.TOKOCHAT_DETAIL,
            TrackAppUtils.EVENT_LABEL to "$channelId - $role - $source",
            TokoChatAnalyticsConstants.TRACKER_ID to TokoChatAnalyticsConstants.TRACKER_ID_39073,
            TokoChatAnalyticsConstants.BUSSINESS_UNIT to TokoChatAnalyticsConstants.COMMUNICATION,
            TokoChatAnalyticsConstants.CURRENT_SITE to TokoChatAnalyticsConstants.TOKOPEDIA_MARKETPLACE,
        )
        tracking.sendGeneralEvent(mapData)
    }

    fun clickConfirmCallOnBottomSheetCallDriver(
        orderStatus: String,
        orderId: String,
        channelId: String,
        source: String,
        role: String
    ) {
        val mapData = mapOf(
            TrackAppUtils.EVENT to TokoChatAnalyticsConstants.CLICK_COMMUNICATION,
            TrackAppUtils.EVENT_ACTION to TokoChatAnalyticsConstants.CLICK_CONFIRM_CALL_ON_BOTTOM_SHEET_CALL_DRIVER,
            TrackAppUtils.EVENT_CATEGORY to TokoChatAnalyticsConstants.TOKOCHAT_DETAIL,
            TrackAppUtils.EVENT_LABEL to "$orderStatus - $orderId - $channelId - $source - $role",
            TokoChatAnalyticsConstants.TRACKER_ID to TokoChatAnalyticsConstants.TRACKER_ID_39074,
            TokoChatAnalyticsConstants.BUSSINESS_UNIT to TokoChatAnalyticsConstants.COMMUNICATION,
            TokoChatAnalyticsConstants.CURRENT_SITE to TokoChatAnalyticsConstants.TOKOPEDIA_MARKETPLACE,
        )
        tracking.sendGeneralEvent(mapData)
    }

    fun clickCloseBottomSheetCallDriver(
        orderStatus: String,
        orderId: String,
        channelId: String,
        source: String,
        role: String
    ) {
        val mapData = mapOf(
            TrackAppUtils.EVENT to TokoChatAnalyticsConstants.CLICK_COMMUNICATION,
            TrackAppUtils.EVENT_ACTION to TokoChatAnalyticsConstants.CLICK_CLOSE_BOTTOM_SHEET_CALL_DRIVER,
            TrackAppUtils.EVENT_CATEGORY to TokoChatAnalyticsConstants.TOKOCHAT_DETAIL,
            TrackAppUtils.EVENT_LABEL to "$orderStatus - $orderId - $channelId - $source - $role",
            TokoChatAnalyticsConstants.TRACKER_ID to TokoChatAnalyticsConstants.TRACKER_ID_39075,
            TokoChatAnalyticsConstants.BUSSINESS_UNIT to TokoChatAnalyticsConstants.COMMUNICATION,
            TokoChatAnalyticsConstants.CURRENT_SITE to TokoChatAnalyticsConstants.TOKOPEDIA_MARKETPLACE,
        )
        tracking.sendGeneralEvent(mapData)
    }

    fun clickChatFromPushNotif(
        channelId: String,
        orderId: String,
        templateKey: String,
        role: String,
        source: String
    ) {
        val mapData = mapOf(
            TrackAppUtils.EVENT to TokoChatAnalyticsConstants.CLICK_COMMUNICATION,
            TrackAppUtils.EVENT_ACTION to TokoChatAnalyticsConstants.CLICK_CHAT_FROM_PUSH_NOTIF_TOKOCHAT,
            TrackAppUtils.EVENT_CATEGORY to TokoChatAnalyticsConstants.PUSH_NOTIFICATION_CHAT,
            TrackAppUtils.EVENT_LABEL to "$channelId - $orderId - $templateKey - $role - $source",
            TokoChatAnalyticsConstants.TRACKER_ID to TokoChatAnalyticsConstants.TRACKER_ID_39067,
            TokoChatAnalyticsConstants.BUSSINESS_UNIT to TokoChatAnalyticsConstants.COMMUNICATION,
            TokoChatAnalyticsConstants.CURRENT_SITE to TokoChatAnalyticsConstants.TOKOPEDIA_MARKETPLACE,
        )
        tracking.sendGeneralEvent(mapData)
    }

    fun clickTextField(
        channelId: String,
        role: String,
        source: String
    ) {
        val mapData = mapOf(
            TrackAppUtils.EVENT to TokoChatAnalyticsConstants.CLICK_COMMUNICATION,
            TrackAppUtils.EVENT_ACTION to TokoChatAnalyticsConstants.CLICK_TEXT_FIELD,
            TrackAppUtils.EVENT_CATEGORY to TokoChatAnalyticsConstants.TOKOCHAT_DETAIL,
            TrackAppUtils.EVENT_LABEL to "$channelId - $role - $source",
            TokoChatAnalyticsConstants.TRACKER_ID to TokoChatAnalyticsConstants.TRACKER_ID_39070,
            TokoChatAnalyticsConstants.BUSSINESS_UNIT to TokoChatAnalyticsConstants.COMMUNICATION,
            TokoChatAnalyticsConstants.CURRENT_SITE to TokoChatAnalyticsConstants.TOKOPEDIA_MARKETPLACE,
        )
        tracking.sendGeneralEvent(mapData)
    }

    fun impressOnTicker(
        channelId: String,
        role: String,
        source: String
    ) {
        val mapData = mapOf(
            TrackAppUtils.EVENT to TokoChatAnalyticsConstants.VIEW_COMMUNICATION_IRIS,
            TrackAppUtils.EVENT_ACTION to TokoChatAnalyticsConstants.TICKER_IMPRESSION,
            TrackAppUtils.EVENT_CATEGORY to TokoChatAnalyticsConstants.TOKOCHAT_DETAIL,
            TrackAppUtils.EVENT_LABEL to "$channelId - $role - $source",
            TokoChatAnalyticsConstants.TRACKER_ID to TokoChatAnalyticsConstants.TRACKER_ID_39076,
            TokoChatAnalyticsConstants.BUSSINESS_UNIT to TokoChatAnalyticsConstants.COMMUNICATION,
            TokoChatAnalyticsConstants.CURRENT_SITE to TokoChatAnalyticsConstants.TOKOPEDIA_MARKETPLACE,
        )
        tracking.sendGeneralEvent(mapData)
    }

    fun impressOnImageAttachment(
        attachmentId: String,
        orderStatus: String,
        orderId: String,
        channelId: String,
        role: String,
        source: String
    ) {
        val mapData = mapOf(
            TrackAppUtils.EVENT to TokoChatAnalyticsConstants.VIEW_COMMUNICATION_IRIS,
            TrackAppUtils.EVENT_ACTION to TokoChatAnalyticsConstants.IMPRESSION_ON_IMAGE_ATTACHMENT,
            TrackAppUtils.EVENT_CATEGORY to TokoChatAnalyticsConstants.TOKOCHAT_DETAIL,
            TrackAppUtils.EVENT_LABEL to "$attachmentId - $orderStatus - $orderId - $channelId - $source - $role",
            TokoChatAnalyticsConstants.TRACKER_ID to TokoChatAnalyticsConstants.TRACKER_ID_39077,
            TokoChatAnalyticsConstants.BUSSINESS_UNIT to TokoChatAnalyticsConstants.COMMUNICATION,
            TokoChatAnalyticsConstants.CURRENT_SITE to TokoChatAnalyticsConstants.TOKOPEDIA_MARKETPLACE,
        )
        tracking.sendGeneralEvent(mapData)
    }

    fun clickImageAttachment(
        attachmentId: String,
        orderStatus: String,
        orderId: String,
        channelId: String,
        role: String,
        source: String
    ) {
        val mapData = mapOf(
            TrackAppUtils.EVENT to TokoChatAnalyticsConstants.CLICK_COMMUNICATION,
            TrackAppUtils.EVENT_ACTION to TokoChatAnalyticsConstants.CLICK_ON_IMAGE_ATTACHMENT,
            TrackAppUtils.EVENT_CATEGORY to TokoChatAnalyticsConstants.TOKOCHAT_DETAIL,
            TrackAppUtils.EVENT_LABEL to "$attachmentId - $orderStatus - $orderId - $channelId - $source - $role",
            TokoChatAnalyticsConstants.TRACKER_ID to TokoChatAnalyticsConstants.TRACKER_ID_39078,
            TokoChatAnalyticsConstants.BUSSINESS_UNIT to TokoChatAnalyticsConstants.COMMUNICATION,
            TokoChatAnalyticsConstants.CURRENT_SITE to TokoChatAnalyticsConstants.TOKOPEDIA_MARKETPLACE,
        )
        tracking.sendGeneralEvent(mapData)
    }

    fun impressOnImagePreview(
        attachmentId: String,
        orderStatus: String,
        orderId: String,
        channelId: String,
        role: String,
        source: String
    ) {
        val mapData = mapOf(
            TrackAppUtils.EVENT to TokoChatAnalyticsConstants.VIEW_COMMUNICATION_IRIS,
            TrackAppUtils.EVENT_ACTION to TokoChatAnalyticsConstants.IMPRESSION_ON_IMAGE_PREVIEW,
            TrackAppUtils.EVENT_CATEGORY to TokoChatAnalyticsConstants.TOKOCHAT_DETAIL,
            TrackAppUtils.EVENT_LABEL to "$attachmentId - $orderStatus - $orderId - $channelId - $source - $role",
            TokoChatAnalyticsConstants.TRACKER_ID to TokoChatAnalyticsConstants.TRACKER_ID_39079,
            TokoChatAnalyticsConstants.BUSSINESS_UNIT to TokoChatAnalyticsConstants.COMMUNICATION,
            TokoChatAnalyticsConstants.CURRENT_SITE to TokoChatAnalyticsConstants.TOKOPEDIA_MARKETPLACE,
        )
        tracking.sendGeneralEvent(mapData)
    }
}
