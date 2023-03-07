package com.tokopedia.topchat.chatlist.analytic

import com.tokopedia.topchat.chatlist.domain.pojo.ItemChatListPojo
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class ChatListAnalytic @Inject constructor(
    private val userSession: UserSessionInterface
) {
    interface Event {
        companion object {
            const val CLICK_INBOX_CHAT = "clickInboxChat"
            const val CLICK_CHAT_DETAIL = "clickChatDetail"
            const val VIEW_CHAT_DETAIL_IRIS = "viewChatDetailIris"
            const val CLICK_COMMUNICATION = "clickCommunication"
            const val VIEW_COMMUNICATION_IRIS = "viewCommunicationIris"
        }
    }

    interface Category {
        companion object {
            const val CATEGORY_INBOX_CHAT = "inbox-chat"
            const val CATEGORY_CHAT_DETAIL = "chat detail"
            const val INBOX_PAGE = "inbox page"
        }
    }

    interface Action {
        companion object {
            const val ACTION_SEARCH_ON_CHATLIST = "search on chatlist"
            const val ACTION_CLICK_ON_FILTER = "click on filter"
            const val ACTION_CLICK_ON_LIST_FILTER_CHAT = "click on list filter chat"
            const val ACTION_CLICK_ON_GEAR_ICON_SETTING = "click on gear icon setting"
            const val ACTION_CLICK_TAB_CHAT_ON_INBOX_CHAT = "click tab chat on inbox chat"
            const val ACTION_CLICK_ON_CHATLIST = "click on chatlist"
            const val ACTION_CLICK_ON_MARK_MESSAGE = "click on mark message"
            const val ACTION_CLICK_BROADCAST_WIZARD = "click on broadcast wizard"
            const val DELETE_CHAT = "click on delete chat"
            const val VIEW_CTA_TOPADS = "view cta iklan promosi"
            const val CLICK_CTA_TOPADS = "click coba iklan promosi"
            const val IMPRESSION_ON_CHAT_DRIVER_TICKER = "impression on chat driver ticker"
            const val CLICK_CHAT_DRIVER_TICKER = "click chat driver ticker"
        }
    }

    interface Other {
        companion object {
            const val BUYER = "buyer"
            const val SELLER = "seller"
            const val BUSSINESS_UNIT = "businessUnit"
            const val CURRENT_SITE = "currentSite"
            const val COMMUNICATION = "communication"
            const val TOKOPEDIA_MARKETPLACE = "tokopediamarketplace"
            const val TRACKER_ID = "trackerId"

            const val TRACKER_ID_39092 = "39092"

            const val TRACKER_ID_39093 = "39093"
        }
    }

    //    #CL2
    fun eventClickFilterChat() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            TrackAppUtils.gtmData(
                Event.CLICK_INBOX_CHAT,
                Category.CATEGORY_CHAT_DETAIL,
                Action.ACTION_CLICK_ON_FILTER,
                ""
            )
        )
    }

    //    #CL3
    fun eventClickListFilterChat(label: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            TrackAppUtils.gtmData(
                Event.CLICK_INBOX_CHAT,
                Category.CATEGORY_CHAT_DETAIL,
                Action.ACTION_CLICK_ON_LIST_FILTER_CHAT,
                label
            )
        )
    }

    //    #CL5
    fun eventClickTabChat(label: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            TrackAppUtils.gtmData(
                Event.CLICK_INBOX_CHAT,
                Category.CATEGORY_INBOX_CHAT,
                Action.ACTION_CLICK_TAB_CHAT_ON_INBOX_CHAT,
                label
            )
        )
    }

    //    #CL6
    fun eventClickChatList(label: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            TrackAppUtils.gtmData(
                Event.CLICK_INBOX_CHAT,
                Category.CATEGORY_INBOX_CHAT,
                Action.ACTION_CLICK_ON_CHATLIST,
                label
            )
        )
    }

    fun eventClickBroadcastButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            TrackAppUtils.gtmData(
                Event.CLICK_INBOX_CHAT,
                Category.CATEGORY_INBOX_CHAT,
                Action.ACTION_CLICK_BROADCAST_WIZARD,
                ""
            )
        )
    }

    fun trackChangeReadStatus(element: ItemChatListPojo) {
        val eventLabel = "${element.getLiteralReadStatus()} - ${element.getLiteralUserType()}"
        TrackApp.getInstance().gtm.sendGeneralEvent(
            TrackAppUtils.gtmData(
                Event.CLICK_INBOX_CHAT,
                Category.CATEGORY_INBOX_CHAT,
                Action.ACTION_CLICK_ON_MARK_MESSAGE,
                eventLabel
            )
        )
    }

    fun trackDeleteChat(element: ItemChatListPojo) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            TrackAppUtils.gtmData(
                Event.CLICK_CHAT_DETAIL,
                Category.CATEGORY_CHAT_DETAIL,
                Action.DELETE_CHAT,
                ""
            )
        )
    }

    // #TA1
    fun eventViewCtaTopAds() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            TrackAppUtils.gtmData(
                Event.VIEW_CHAT_DETAIL_IRIS,
                Category.CATEGORY_CHAT_DETAIL,
                Action.VIEW_CTA_TOPADS,
                userSession.shopId
            )
        )
    }

    // #TA2
    fun eventClickCtaTopAds() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            TrackAppUtils.gtmData(
                Event.CLICK_CHAT_DETAIL,
                Category.CATEGORY_CHAT_DETAIL,
                Action.CLICK_CTA_TOPADS,
                userSession.shopId
            )
        )
    }

    fun impressOnChatDriverTicker(role: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            TrackAppUtils.gtmData(
                Event.VIEW_COMMUNICATION_IRIS,
                Category.INBOX_PAGE,
                Action.IMPRESSION_ON_CHAT_DRIVER_TICKER,
                role
            ).apply {
                put(Other.TRACKER_ID, Other.TRACKER_ID_39092)
                put(Other.BUSSINESS_UNIT, Other.COMMUNICATION)
                put(Other.CURRENT_SITE, Other.TOKOPEDIA_MARKETPLACE)
            }
        )
    }

    fun clickChatDriverTicker(role: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            TrackAppUtils.gtmData(
                Event.CLICK_COMMUNICATION,
                Category.INBOX_PAGE,
                Action.CLICK_CHAT_DRIVER_TICKER,
                role
            ).apply {
                put(Other.TRACKER_ID, Other.TRACKER_ID_39093)
                put(Other.BUSSINESS_UNIT, Other.COMMUNICATION)
                put(Other.CURRENT_SITE, Other.TOKOPEDIA_MARKETPLACE)
            }
        )
    }
}
