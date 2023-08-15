package com.tokopedia.tokochat.analytics

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.interfaces.ContextAnalytics

class TokoChatAnalytics(private val isFromBubble: Boolean = false) {

    private val tracking: ContextAnalytics by lazy(LazyThreadSafetyMode.NONE) { TrackApp.getInstance().gtm }
    private val pendingTracker: ArrayList<MutableMap<String, String>> = arrayListOf()

    fun getStringRole(isSender: Boolean): String {
        return if (isSender) {
            TokoChatAnalyticsConstants.BUYER
        } else {
            TokoChatAnalyticsConstants.DRIVER
        }
    }

    fun clickCallButtonFromChatRoom(
        orderStatus: String,
        orderId: String,
        source: String,
        role: String
    ) {
        val eventCategory = if (isFromBubble) {
            TokoChatAnalyticsConstants.TOKOCHAT_BUBBLE_CHATROOM
        } else {
            TokoChatAnalyticsConstants.TOKOCHAT_DETAIL
        }
        val trackerId = if (isFromBubble) {
            TokoChatAnalyticsConstants.TRACKER_ID_43168
        } else {
            TokoChatAnalyticsConstants.TRACKER_ID_39069
        }
        val mapData = mapOf(
            TrackAppUtils.EVENT to TokoChatAnalyticsConstants.CLICK_COMMUNICATION,
            TrackAppUtils.EVENT_ACTION to TokoChatAnalyticsConstants.CLICK_CALL_BUTTON_FROM_CHATROOM,
            TrackAppUtils.EVENT_CATEGORY to eventCategory,
            TrackAppUtils.EVENT_LABEL to "$orderStatus - $orderId - $source - $role",
            TokoChatAnalyticsConstants.TRACKER_ID to trackerId,
            TokoChatAnalyticsConstants.BUSSINESS_UNIT to TokoChatAnalyticsConstants.COMMUNICATION,
            TokoChatAnalyticsConstants.CURRENT_SITE to TokoChatAnalyticsConstants.TOKOPEDIA_MARKETPLACE
        )
        tracking.sendGeneralEvent(mapData)
    }

    fun clickCloseOrderWidget(
        orderId: String,
        role: String,
        source: String
    ) {
        val eventCategory = if (isFromBubble) {
            TokoChatAnalyticsConstants.TOKOCHAT_BUBBLE_CHATROOM
        } else {
            TokoChatAnalyticsConstants.TOKOCHAT_DETAIL
        }
        val trackerId = if (isFromBubble) {
            TokoChatAnalyticsConstants.TRACKER_ID_43170
        } else {
            TokoChatAnalyticsConstants.TRACKER_ID_39071
        }
        val mapData = mapOf(
            TrackAppUtils.EVENT to TokoChatAnalyticsConstants.CLICK_COMMUNICATION,
            TrackAppUtils.EVENT_ACTION to TokoChatAnalyticsConstants.CLICK_CLOSE_ORDER_WIDGET,
            TrackAppUtils.EVENT_CATEGORY to eventCategory,
            TrackAppUtils.EVENT_LABEL to "$orderId - $role - $source",
            TokoChatAnalyticsConstants.TRACKER_ID to trackerId,
            TokoChatAnalyticsConstants.BUSSINESS_UNIT to TokoChatAnalyticsConstants.COMMUNICATION,
            TokoChatAnalyticsConstants.CURRENT_SITE to TokoChatAnalyticsConstants.TOKOPEDIA_MARKETPLACE
        )
        tracking.sendGeneralEvent(mapData)
    }

    fun clickSendMessage(
        orderId: String,
        role: String,
        source: String
    ) {
        val eventCategory = if (isFromBubble) {
            TokoChatAnalyticsConstants.TOKOCHAT_BUBBLE_CHATROOM
        } else {
            TokoChatAnalyticsConstants.TOKOCHAT_DETAIL
        }
        val trackerId = if (isFromBubble) {
            TokoChatAnalyticsConstants.TRACKER_ID_43171
        } else {
            TokoChatAnalyticsConstants.TRACKER_ID_39072
        }
        val mapData = mapOf(
            TrackAppUtils.EVENT to TokoChatAnalyticsConstants.CLICK_COMMUNICATION,
            TrackAppUtils.EVENT_ACTION to TokoChatAnalyticsConstants.CLICK_SEND_MESSAGE,
            TrackAppUtils.EVENT_CATEGORY to eventCategory,
            TrackAppUtils.EVENT_LABEL to "$orderId - $role - $source",
            TokoChatAnalyticsConstants.TRACKER_ID to trackerId,
            TokoChatAnalyticsConstants.BUSSINESS_UNIT to TokoChatAnalyticsConstants.COMMUNICATION,
            TokoChatAnalyticsConstants.CURRENT_SITE to TokoChatAnalyticsConstants.TOKOPEDIA_MARKETPLACE
        )
        tracking.sendGeneralEvent(mapData)
    }

    fun impressOnClosedChatroomTicker(
        orderId: String,
        role: String,
        source: String
    ) {
        val eventCategory = if (isFromBubble) {
            TokoChatAnalyticsConstants.TOKOCHAT_BUBBLE_CHATROOM
        } else {
            TokoChatAnalyticsConstants.TOKOCHAT_DETAIL
        }
        val trackerId = if (isFromBubble) {
            TokoChatAnalyticsConstants.TRACKER_ID_43172
        } else {
            TokoChatAnalyticsConstants.TRACKER_ID_39073
        }
        val mapData = mapOf(
            TrackAppUtils.EVENT to TokoChatAnalyticsConstants.VIEW_COMMUNICATION_IRIS,
            TrackAppUtils.EVENT_ACTION to TokoChatAnalyticsConstants.IMPRESSION_ON_CLOSED_CHATROM_TICKER,
            TrackAppUtils.EVENT_CATEGORY to eventCategory,
            TrackAppUtils.EVENT_LABEL to "$orderId - $role - $source",
            TokoChatAnalyticsConstants.TRACKER_ID to trackerId,
            TokoChatAnalyticsConstants.BUSSINESS_UNIT to TokoChatAnalyticsConstants.COMMUNICATION,
            TokoChatAnalyticsConstants.CURRENT_SITE to TokoChatAnalyticsConstants.TOKOPEDIA_MARKETPLACE
        )
        tracking.sendGeneralEvent(mapData)
    }

    fun clickConfirmCallOnBottomSheetCallDriver(
        orderStatus: String,
        orderId: String,
        source: String,
        role: String
    ) {
        val eventCategory = if (isFromBubble) {
            TokoChatAnalyticsConstants.TOKOCHAT_BUBBLE_CHATROOM
        } else {
            TokoChatAnalyticsConstants.TOKOCHAT_DETAIL
        }
        val trackerId = if (isFromBubble) {
            TokoChatAnalyticsConstants.TRACKER_ID_43173
        } else {
            TokoChatAnalyticsConstants.TRACKER_ID_39074
        }
        val mapData = mapOf(
            TrackAppUtils.EVENT to TokoChatAnalyticsConstants.CLICK_COMMUNICATION,
            TrackAppUtils.EVENT_ACTION to TokoChatAnalyticsConstants.CLICK_CONFIRM_CALL_ON_BOTTOM_SHEET_CALL_DRIVER,
            TrackAppUtils.EVENT_CATEGORY to eventCategory,
            TrackAppUtils.EVENT_LABEL to "$orderStatus - $orderId - $source - $role",
            TokoChatAnalyticsConstants.TRACKER_ID to trackerId,
            TokoChatAnalyticsConstants.BUSSINESS_UNIT to TokoChatAnalyticsConstants.COMMUNICATION,
            TokoChatAnalyticsConstants.CURRENT_SITE to TokoChatAnalyticsConstants.TOKOPEDIA_MARKETPLACE
        )
        tracking.sendGeneralEvent(mapData)
    }

    fun clickCloseBottomSheetCallDriver(
        orderStatus: String,
        orderId: String,
        source: String,
        role: String
    ) {
        val eventCategory = if (isFromBubble) {
            TokoChatAnalyticsConstants.TOKOCHAT_BUBBLE_CHATROOM
        } else {
            TokoChatAnalyticsConstants.TOKOCHAT_DETAIL
        }
        val trackerId = if (isFromBubble) {
            TokoChatAnalyticsConstants.TRACKER_ID_43174
        } else {
            TokoChatAnalyticsConstants.TRACKER_ID_39075
        }
        val mapData = mapOf(
            TrackAppUtils.EVENT to TokoChatAnalyticsConstants.CLICK_COMMUNICATION,
            TrackAppUtils.EVENT_ACTION to TokoChatAnalyticsConstants.CLICK_CLOSE_BOTTOM_SHEET_CALL_DRIVER,
            TrackAppUtils.EVENT_CATEGORY to eventCategory,
            TrackAppUtils.EVENT_LABEL to "$orderStatus - $orderId - $source - $role",
            TokoChatAnalyticsConstants.TRACKER_ID to trackerId,
            TokoChatAnalyticsConstants.BUSSINESS_UNIT to TokoChatAnalyticsConstants.COMMUNICATION,
            TokoChatAnalyticsConstants.CURRENT_SITE to TokoChatAnalyticsConstants.TOKOPEDIA_MARKETPLACE
        )
        tracking.sendGeneralEvent(mapData)
    }

    fun clickChatFromPushNotif(
        orderId: String,
        templateKey: String,
        role: String,
        source: String
    ) {
        val mapData = mapOf(
            TrackAppUtils.EVENT to TokoChatAnalyticsConstants.CLICK_COMMUNICATION,
            TrackAppUtils.EVENT_ACTION to TokoChatAnalyticsConstants.CLICK_CHAT_FROM_PUSH_NOTIF_TOKOCHAT,
            TrackAppUtils.EVENT_CATEGORY to TokoChatAnalyticsConstants.PUSH_NOTIFICATION_CHAT,
            TrackAppUtils.EVENT_LABEL to "$orderId - $templateKey - $role - $source",
            TokoChatAnalyticsConstants.TRACKER_ID to TokoChatAnalyticsConstants.TRACKER_ID_39067,
            TokoChatAnalyticsConstants.BUSSINESS_UNIT to TokoChatAnalyticsConstants.COMMUNICATION,
            TokoChatAnalyticsConstants.CURRENT_SITE to TokoChatAnalyticsConstants.TOKOPEDIA_MARKETPLACE
        )
        tracking.sendGeneralEvent(mapData)
    }

    fun clickTextField(
        orderId: String,
        role: String,
        source: String
    ) {
        val eventCategory = if (isFromBubble) {
            TokoChatAnalyticsConstants.TOKOCHAT_BUBBLE_CHATROOM
        } else {
            TokoChatAnalyticsConstants.TOKOCHAT_DETAIL
        }
        val trackerId = if (isFromBubble) {
            TokoChatAnalyticsConstants.TRACKER_ID_43169
        } else {
            TokoChatAnalyticsConstants.TRACKER_ID_39070
        }
        val mapData = mapOf(
            TrackAppUtils.EVENT to TokoChatAnalyticsConstants.CLICK_COMMUNICATION,
            TrackAppUtils.EVENT_ACTION to TokoChatAnalyticsConstants.CLICK_TEXT_FIELD,
            TrackAppUtils.EVENT_CATEGORY to eventCategory,
            TrackAppUtils.EVENT_LABEL to "$orderId - $role - $source",
            TokoChatAnalyticsConstants.TRACKER_ID to trackerId,
            TokoChatAnalyticsConstants.BUSSINESS_UNIT to TokoChatAnalyticsConstants.COMMUNICATION,
            TokoChatAnalyticsConstants.CURRENT_SITE to TokoChatAnalyticsConstants.TOKOPEDIA_MARKETPLACE
        )
        tracking.sendGeneralEvent(mapData)
    }

    fun impressOnTicker(
        orderId: String,
        role: String,
        source: String
    ) {
        val eventCategory = if (isFromBubble) {
            TokoChatAnalyticsConstants.TOKOCHAT_BUBBLE_CHATROOM
        } else {
            TokoChatAnalyticsConstants.TOKOCHAT_DETAIL
        }
        val trackerId = if (isFromBubble) {
            TokoChatAnalyticsConstants.TRACKER_ID_43175
        } else {
            TokoChatAnalyticsConstants.TRACKER_ID_39076
        }
        val mapData = mapOf(
            TrackAppUtils.EVENT to TokoChatAnalyticsConstants.VIEW_COMMUNICATION_IRIS,
            TrackAppUtils.EVENT_ACTION to TokoChatAnalyticsConstants.TICKER_IMPRESSION,
            TrackAppUtils.EVENT_CATEGORY to eventCategory,
            TrackAppUtils.EVENT_LABEL to "$orderId - $role - $source",
            TokoChatAnalyticsConstants.TRACKER_ID to trackerId,
            TokoChatAnalyticsConstants.BUSSINESS_UNIT to TokoChatAnalyticsConstants.COMMUNICATION,
            TokoChatAnalyticsConstants.CURRENT_SITE to TokoChatAnalyticsConstants.TOKOPEDIA_MARKETPLACE
        )
        tracking.sendGeneralEvent(mapData)
    }

    fun impressOnImageAttachment(
        attachmentId: String,
        orderStatus: String,
        orderId: String,
        role: String,
        source: String
    ) {
        val mapData = createMapDataImpressImageAttachment(
            attachmentId,
            orderStatus,
            orderId,
            role,
            source
        )
        tracking.sendGeneralEvent(mapData.toMap())
    }

    fun saveImpressionOnImageAttachment(
        attachmentId: String,
        orderStatus: String,
        orderId: String,
        role: String,
        source: String
    ) {
        val mutableMap = createMapDataImpressImageAttachment(
            attachmentId,
            orderStatus,
            orderId,
            role,
            source
        )
        mutableMap[TokoChatAnalyticsConstants.ATTACHMENT_ID] = attachmentId
        mutableMap[TokoChatAnalyticsConstants.ORDER_ID] = orderId
        mutableMap[TokoChatAnalyticsConstants.ROLE] = role
        mutableMap[TokoChatAnalyticsConstants.SOURCE] = source
        pendingTracker.add(mutableMap)
    }

    fun sendPendingImpressionOnImageAttachment(orderStatus: String) {
        val iterator = pendingTracker.iterator()
        while (iterator.hasNext()) {
            val mapData = iterator.next()
            if (mapData[TrackAppUtils.EVENT_ACTION] ==
                TokoChatAnalyticsConstants.IMPRESSION_ON_IMAGE_ATTACHMENT
            ) {
                val attachmentId = mapData[TokoChatAnalyticsConstants.ATTACHMENT_ID]
                mapData.remove(TokoChatAnalyticsConstants.ATTACHMENT_ID)
                val orderId = mapData[TokoChatAnalyticsConstants.ORDER_ID]
                mapData.remove(TokoChatAnalyticsConstants.ORDER_ID)
                val source = mapData[TokoChatAnalyticsConstants.SOURCE]
                mapData.remove(TokoChatAnalyticsConstants.SOURCE)
                val role = mapData[TokoChatAnalyticsConstants.ROLE]
                mapData.remove(TokoChatAnalyticsConstants.ROLE)
                mapData.remove(TokoChatAnalyticsConstants.ORDER_STATUS)
                mapData[TrackAppUtils.EVENT_LABEL] =
                    "$attachmentId - $orderStatus - $orderId - $source - $role"
                tracking.sendGeneralEvent(mapData.toMap())
                iterator.remove()
            }
        }
    }

    private fun createMapDataImpressImageAttachment(
        attachmentId: String,
        orderStatus: String,
        orderId: String,
        role: String,
        source: String
    ): MutableMap<String, String> {
        val eventCategory = if (isFromBubble) {
            TokoChatAnalyticsConstants.TOKOCHAT_BUBBLE_CHATROOM
        } else {
            TokoChatAnalyticsConstants.TOKOCHAT_DETAIL
        }
        val trackerId = if (isFromBubble) {
            TokoChatAnalyticsConstants.TRACKER_ID_43176
        } else {
            TokoChatAnalyticsConstants.TRACKER_ID_39077
        }
        return mutableMapOf(
            TrackAppUtils.EVENT to TokoChatAnalyticsConstants.VIEW_COMMUNICATION_IRIS,
            TrackAppUtils.EVENT_ACTION to TokoChatAnalyticsConstants.IMPRESSION_ON_IMAGE_ATTACHMENT,
            TrackAppUtils.EVENT_CATEGORY to eventCategory,
            TrackAppUtils.EVENT_LABEL to "$attachmentId - $orderStatus - $orderId - $source - $role",
            TokoChatAnalyticsConstants.TRACKER_ID to trackerId,
            TokoChatAnalyticsConstants.BUSSINESS_UNIT to TokoChatAnalyticsConstants.COMMUNICATION,
            TokoChatAnalyticsConstants.CURRENT_SITE to TokoChatAnalyticsConstants.TOKOPEDIA_MARKETPLACE
        )
    }

    fun clickImageAttachment(
        attachmentId: String,
        orderStatus: String,
        orderId: String,
        role: String,
        source: String
    ) {
        val eventCategory = if (isFromBubble) {
            TokoChatAnalyticsConstants.TOKOCHAT_BUBBLE_CHATROOM
        } else {
            TokoChatAnalyticsConstants.TOKOCHAT_DETAIL
        }
        val trackerId = if (isFromBubble) {
            TokoChatAnalyticsConstants.TRACKER_ID_43177
        } else {
            TokoChatAnalyticsConstants.TRACKER_ID_39078
        }
        val mapData = mapOf(
            TrackAppUtils.EVENT to TokoChatAnalyticsConstants.CLICK_COMMUNICATION,
            TrackAppUtils.EVENT_ACTION to TokoChatAnalyticsConstants.CLICK_ON_IMAGE_ATTACHMENT,
            TrackAppUtils.EVENT_CATEGORY to eventCategory,
            TrackAppUtils.EVENT_LABEL to "$attachmentId - $orderStatus - $orderId - $source - $role",
            TokoChatAnalyticsConstants.TRACKER_ID to trackerId,
            TokoChatAnalyticsConstants.BUSSINESS_UNIT to TokoChatAnalyticsConstants.COMMUNICATION,
            TokoChatAnalyticsConstants.CURRENT_SITE to TokoChatAnalyticsConstants.TOKOPEDIA_MARKETPLACE
        )
        tracking.sendGeneralEvent(mapData)
    }

    fun impressOnImagePreview(
        attachmentId: String,
        orderStatus: String,
        orderId: String,
        role: String,
        source: String
    ) {
        val eventCategory = if (isFromBubble) {
            TokoChatAnalyticsConstants.TOKOCHAT_BUBBLE_CHATROOM
        } else {
            TokoChatAnalyticsConstants.TOKOCHAT_DETAIL
        }
        val trackerId = if (isFromBubble) {
            TokoChatAnalyticsConstants.TRACKER_ID_43178
        } else {
            TokoChatAnalyticsConstants.TRACKER_ID_39079
        }
        val mapData = mapOf(
            TrackAppUtils.EVENT to TokoChatAnalyticsConstants.VIEW_COMMUNICATION_IRIS,
            TrackAppUtils.EVENT_ACTION to TokoChatAnalyticsConstants.IMPRESSION_ON_IMAGE_PREVIEW,
            TrackAppUtils.EVENT_CATEGORY to eventCategory,
            TrackAppUtils.EVENT_LABEL to "$attachmentId - $orderStatus - $orderId - $source - $role",
            TokoChatAnalyticsConstants.TRACKER_ID to trackerId,
            TokoChatAnalyticsConstants.BUSSINESS_UNIT to TokoChatAnalyticsConstants.COMMUNICATION,
            TokoChatAnalyticsConstants.CURRENT_SITE to TokoChatAnalyticsConstants.TOKOPEDIA_MARKETPLACE
        )
        tracking.sendGeneralEvent(mapData)
    }

    fun clickAddImageAttachment(
        orderId: String,
        role: String,
        source: String
    ) {
        val eventCategory = if (isFromBubble) {
            TokoChatAnalyticsConstants.TOKOCHAT_BUBBLE_CHATROOM
        } else {
            TokoChatAnalyticsConstants.TOKOCHAT_DETAIL
        }
        val trackerId = if (isFromBubble) {
            TokoChatAnalyticsConstants.TRACKER_ID_43182
        } else {
            TokoChatAnalyticsConstants.TRACKER_ID_43070
        }
        val mapData = mapOf(
            TrackAppUtils.EVENT to TokoChatAnalyticsConstants.CLICK_COMMUNICATION,
            TrackAppUtils.EVENT_ACTION to TokoChatAnalyticsConstants.CLICK_ADD_IMAGE_ATTACHMENT,
            TrackAppUtils.EVENT_CATEGORY to eventCategory,
            TrackAppUtils.EVENT_LABEL to "$orderId - $source - $role",
            TokoChatAnalyticsConstants.TRACKER_ID to trackerId,
            TokoChatAnalyticsConstants.BUSSINESS_UNIT to TokoChatAnalyticsConstants.COMMUNICATION,
            TokoChatAnalyticsConstants.CURRENT_SITE to TokoChatAnalyticsConstants.TOKOPEDIA_MARKETPLACE
        )
        tracking.sendGeneralEvent(mapData)
    }

    fun clickUploadButton(
        attachmentId: String,
        orderId: String,
        role: String,
        source: String
    ) {
        val eventCategory = if (isFromBubble) {
            TokoChatAnalyticsConstants.TOKOCHAT_BUBBLE_CHATROOM
        } else {
            TokoChatAnalyticsConstants.TOKOCHAT_DETAIL
        }
        val trackerId = if (isFromBubble) {
            TokoChatAnalyticsConstants.TRACKER_ID_43183
        } else {
            TokoChatAnalyticsConstants.TRACKER_ID_43071
        }
        val mapData = mapOf(
            TrackAppUtils.EVENT to TokoChatAnalyticsConstants.CLICK_COMMUNICATION,
            TrackAppUtils.EVENT_ACTION to TokoChatAnalyticsConstants.CLICK_UPLOAD_BUTTON,
            TrackAppUtils.EVENT_CATEGORY to eventCategory,
            TrackAppUtils.EVENT_LABEL to "$attachmentId - $orderId - $source - $role",
            TokoChatAnalyticsConstants.TRACKER_ID to trackerId,
            TokoChatAnalyticsConstants.BUSSINESS_UNIT to TokoChatAnalyticsConstants.COMMUNICATION,
            TokoChatAnalyticsConstants.CURRENT_SITE to TokoChatAnalyticsConstants.TOKOPEDIA_MARKETPLACE
        )
        tracking.sendGeneralEvent(mapData)
    }

    fun clickRetryImage(
        attachmentId: String,
        orderId: String,
        role: String,
        source: String
    ) {
        val eventCategory = if (isFromBubble) {
            TokoChatAnalyticsConstants.TOKOCHAT_BUBBLE_CHATROOM
        } else {
            TokoChatAnalyticsConstants.TOKOCHAT_DETAIL
        }
        val trackerId = if (isFromBubble) {
            TokoChatAnalyticsConstants.TRACKER_ID_43184
        } else {
            TokoChatAnalyticsConstants.TRACKER_ID_43073
        }
        val mapData = mapOf(
            TrackAppUtils.EVENT to TokoChatAnalyticsConstants.CLICK_COMMUNICATION,
            TrackAppUtils.EVENT_ACTION to TokoChatAnalyticsConstants.CLICK_RETRY_IMAGE,
            TrackAppUtils.EVENT_CATEGORY to eventCategory,
            TrackAppUtils.EVENT_LABEL to "$attachmentId - $orderId - $source - $role",
            TokoChatAnalyticsConstants.TRACKER_ID to trackerId,
            TokoChatAnalyticsConstants.BUSSINESS_UNIT to TokoChatAnalyticsConstants.COMMUNICATION,
            TokoChatAnalyticsConstants.CURRENT_SITE to TokoChatAnalyticsConstants.TOKOPEDIA_MARKETPLACE
        )
        tracking.sendGeneralEvent(mapData)
    }

    fun clickDismissConsent(
        role: String,
        source: String
    ) {
        val eventCategory = if (isFromBubble) {
            TokoChatAnalyticsConstants.TOKOCHAT_BUBBLE_CHATROOM
        } else {
            TokoChatAnalyticsConstants.TOKOCHAT_DETAIL
        }
        val trackerId = if (isFromBubble) {
            TokoChatAnalyticsConstants.TRACKER_ID_43181
        } else {
            TokoChatAnalyticsConstants.TRACKER_ID_42872
        }
        val mapData = mapOf(
            TrackAppUtils.EVENT to TokoChatAnalyticsConstants.CLICK_COMMUNICATION,
            TrackAppUtils.EVENT_ACTION to TokoChatAnalyticsConstants.CLICK_CLOSE_ON_CHAT_WITH_DRIVER_CONSENT_TICKER,
            TrackAppUtils.EVENT_CATEGORY to eventCategory,
            TrackAppUtils.EVENT_LABEL to "$role - $source",
            TokoChatAnalyticsConstants.TRACKER_ID to trackerId,
            TokoChatAnalyticsConstants.BUSSINESS_UNIT to TokoChatAnalyticsConstants.COMMUNICATION,
            TokoChatAnalyticsConstants.CURRENT_SITE to TokoChatAnalyticsConstants.TOKOPEDIA_MARKETPLACE
        )
        tracking.sendGeneralEvent(mapData)
    }

    fun viewOnboardingBottomsheet(
        orderId: String,
        role: String,
        source: String
    ) {
        val mapData = mapOf(
            TrackAppUtils.EVENT to TokoChatAnalyticsConstants.VIEW_COMMUNICATION_IRIS,
            TrackAppUtils.EVENT_ACTION to TokoChatAnalyticsConstants.VIEW_ONBOARDING_BOTTOMSHEET,
            TrackAppUtils.EVENT_CATEGORY to TokoChatAnalyticsConstants.TOKOCHAT_BUBBLE_CHATROOM,
            TrackAppUtils.EVENT_LABEL to "$orderId - $source - $role",
            TokoChatAnalyticsConstants.TRACKER_ID to TokoChatAnalyticsConstants.TRACKER_ID_43185,
            TokoChatAnalyticsConstants.BUSSINESS_UNIT to TokoChatAnalyticsConstants.COMMUNICATION,
            TokoChatAnalyticsConstants.CURRENT_SITE to TokoChatAnalyticsConstants.TOKOPEDIA_MARKETPLACE
        )
        tracking.sendGeneralEvent(mapData)
    }

    fun clickContinueOnboardingBottomSheet(
        orderId: String,
        role: String,
        source: String
    ) {
        val mapData = mapOf(
            TrackAppUtils.EVENT to TokoChatAnalyticsConstants.CLICK_COMMUNICATION,
            TrackAppUtils.EVENT_ACTION to TokoChatAnalyticsConstants.CLICK_CONTINUE_ONBOARDING_BOTTOMSHEET,
            TrackAppUtils.EVENT_CATEGORY to TokoChatAnalyticsConstants.TOKOCHAT_BUBBLE_CHATROOM,
            TrackAppUtils.EVENT_LABEL to "$orderId - $source - $role",
            TokoChatAnalyticsConstants.TRACKER_ID to TokoChatAnalyticsConstants.TRACKER_ID_43186,
            TokoChatAnalyticsConstants.BUSSINESS_UNIT to TokoChatAnalyticsConstants.COMMUNICATION,
            TokoChatAnalyticsConstants.CURRENT_SITE to TokoChatAnalyticsConstants.TOKOPEDIA_MARKETPLACE
        )
        tracking.sendGeneralEvent(mapData)
    }

    fun swipeNextOnboardingBottomsheet(
        orderId: String,
        role: String,
        source: String
    ) {
        val mapData = mapOf(
            TrackAppUtils.EVENT to TokoChatAnalyticsConstants.CLICK_COMMUNICATION,
            TrackAppUtils.EVENT_ACTION to TokoChatAnalyticsConstants.SWIPE_NEXT_ONBOARDING_BOTTOMSHEET,
            TrackAppUtils.EVENT_CATEGORY to TokoChatAnalyticsConstants.TOKOCHAT_BUBBLE_CHATROOM,
            TrackAppUtils.EVENT_LABEL to "$orderId - $source - $role",
            TokoChatAnalyticsConstants.TRACKER_ID to TokoChatAnalyticsConstants.TRACKER_ID_43187,
            TokoChatAnalyticsConstants.BUSSINESS_UNIT to TokoChatAnalyticsConstants.COMMUNICATION,
            TokoChatAnalyticsConstants.CURRENT_SITE to TokoChatAnalyticsConstants.TOKOPEDIA_MARKETPLACE
        )
        tracking.sendGeneralEvent(mapData)
    }

    fun clickSelengkapnyaOnboardingBottomSheet(
        orderId: String,
        role: String,
        source: String
    ) {
        val mapData = mapOf(
            TrackAppUtils.EVENT to TokoChatAnalyticsConstants.CLICK_COMMUNICATION,
            TrackAppUtils.EVENT_ACTION to TokoChatAnalyticsConstants.CLICK_SELENGKAPNYA_ONBOARDING_BOTTOMSHEET,
            TrackAppUtils.EVENT_CATEGORY to TokoChatAnalyticsConstants.TOKOCHAT_BUBBLE_CHATROOM,
            TrackAppUtils.EVENT_LABEL to "$orderId - $source - $role",
            TokoChatAnalyticsConstants.TRACKER_ID to TokoChatAnalyticsConstants.TRACKER_ID_43188,
            TokoChatAnalyticsConstants.BUSSINESS_UNIT to TokoChatAnalyticsConstants.COMMUNICATION,
            TokoChatAnalyticsConstants.CURRENT_SITE to TokoChatAnalyticsConstants.TOKOPEDIA_MARKETPLACE
        )
        tracking.sendGeneralEvent(mapData)
    }

    fun clickActivateFromOnboardingBottomSheet(
        orderId: String,
        role: String,
        source: String
    ) {
        val mapData = mapOf(
            TrackAppUtils.EVENT to TokoChatAnalyticsConstants.CLICK_COMMUNICATION,
            TrackAppUtils.EVENT_ACTION to TokoChatAnalyticsConstants.CLICK_ACTIVATE_FROM_ONBOARDING_BOTTOMSHEET,
            TrackAppUtils.EVENT_CATEGORY to TokoChatAnalyticsConstants.TOKOCHAT_BUBBLE_CHATROOM,
            TrackAppUtils.EVENT_LABEL to "$orderId - $source - $role",
            TokoChatAnalyticsConstants.TRACKER_ID to TokoChatAnalyticsConstants.TRACKER_ID_43189,
            TokoChatAnalyticsConstants.BUSSINESS_UNIT to TokoChatAnalyticsConstants.COMMUNICATION,
            TokoChatAnalyticsConstants.CURRENT_SITE to TokoChatAnalyticsConstants.TOKOPEDIA_MARKETPLACE
        )
        tracking.sendGeneralEvent(mapData)
    }

    fun clickCloseOnBoardingTicker(
        orderId: String,
        role: String,
        source: String
    ) {
        val mapData = mapOf(
            TrackAppUtils.EVENT to TokoChatAnalyticsConstants.CLICK_COMMUNICATION,
            TrackAppUtils.EVENT_ACTION to TokoChatAnalyticsConstants.CLICK_CLOSE_ON_BOARDING_TICKER,
            TrackAppUtils.EVENT_CATEGORY to TokoChatAnalyticsConstants.TOKOCHAT_BUBBLE_CHATROOM,
            TrackAppUtils.EVENT_LABEL to "$orderId - $source - $role",
            TokoChatAnalyticsConstants.TRACKER_ID to TokoChatAnalyticsConstants.TRACKER_ID_43191,
            TokoChatAnalyticsConstants.BUSSINESS_UNIT to TokoChatAnalyticsConstants.COMMUNICATION,
            TokoChatAnalyticsConstants.CURRENT_SITE to TokoChatAnalyticsConstants.TOKOPEDIA_MARKETPLACE
        )
        tracking.sendGeneralEvent(mapData)
    }

    fun clickCheckHereOnBoardingTicker(
        orderId: String,
        role: String,
        source: String
    ) {
        val mapData = mapOf(
            TrackAppUtils.EVENT to TokoChatAnalyticsConstants.CLICK_COMMUNICATION,
            TrackAppUtils.EVENT_ACTION to TokoChatAnalyticsConstants.CLICK_CHECK_HERE_ONBOARDING_TICKER,
            TrackAppUtils.EVENT_CATEGORY to TokoChatAnalyticsConstants.TOKOCHAT_BUBBLE_CHATROOM,
            TrackAppUtils.EVENT_LABEL to "$orderId - $source - $role",
            TokoChatAnalyticsConstants.TRACKER_ID to TokoChatAnalyticsConstants.TRACKER_ID_43192,
            TokoChatAnalyticsConstants.BUSSINESS_UNIT to TokoChatAnalyticsConstants.COMMUNICATION,
            TokoChatAnalyticsConstants.CURRENT_SITE to TokoChatAnalyticsConstants.TOKOPEDIA_MARKETPLACE
        )
        tracking.sendGeneralEvent(mapData)
    }
}
