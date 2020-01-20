package com.tokopedia.topchat.chatlist.analytic

import com.tokopedia.topchat.chatlist.pojo.ItemChatListPojo
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import javax.inject.Inject

/**
 * @author : Steven 2019-09-17
 */


class ChatListAnalytic @Inject constructor(){
    interface Event {
        companion object {
            const val CLICK_INBOX_CHAT = "clickInboxChat"
            const val CLICK_CHAT_DETAIL = "clickChatDetail"
        }
    }

    interface Category {
        companion object {
            const val CATEGORY_INBOX_CHAT = "inbox-chat"
            const val CATEGORY_CHAT_DETAIL = "chat detail"
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
        }
    }


//    #CL2
    fun eventClickFilterChat() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_INBOX_CHAT,
                Category.CATEGORY_CHAT_DETAIL,
                Action.ACTION_CLICK_ON_FILTER,
                ""
        ))
    }

//    #CL3
    fun eventClickListFilterChat(label: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_INBOX_CHAT,
                Category.CATEGORY_CHAT_DETAIL,
                Action.ACTION_CLICK_ON_LIST_FILTER_CHAT,
                label
        ))
    }
//    #CL5
    fun eventClickTabChat(label: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_INBOX_CHAT,
                Category.CATEGORY_INBOX_CHAT,
                Action.ACTION_CLICK_TAB_CHAT_ON_INBOX_CHAT,
                label
        ))
    }

//    #CL6
    fun eventClickChatList(label: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_INBOX_CHAT,
                Category.CATEGORY_INBOX_CHAT,
                Action.ACTION_CLICK_ON_CHATLIST,
                label
        ))
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
}