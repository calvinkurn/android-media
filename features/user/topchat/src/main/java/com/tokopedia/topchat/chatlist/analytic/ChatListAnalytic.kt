package com.tokopedia.topchat.chatlist.analytic

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import javax.inject.Inject

/**
 * @author : Steven 2019-09-17
 */


class ChatListAnalytic @Inject constructor(){
    interface Event {
        companion object {
            val CLICK_INBOX_CHAT = "clickInboxChat"
        }
    }

    interface Category {
        companion object {
            val CATEGORY_INBOX_CHAT = "inbox-chat"
            val CATEGORY_CHAT_DETAIL = "chat detail"
        }
    }

    interface Action {
        companion object {
            val ACTION_SEARCH_ON_CHATLIST = "search on chatlist"
            val ACTION_CLICK_ON_FILTER = "click on filter"
            val ACTION_CLICK_ON_LIST_FILTER_CHAT = "click on list filter chat"
            val ACTION_CLICK_ON_GEAR_ICON_SETTING = "click on gear icon setting"
            val ACTION_CLICK_TAB_CHAT_ON_INBOX_CHAT = "click tab chat on inbox chat"
            val ACTION_CLICK_ON_CHATLIST = "click on chatlist"
            val ACTION_CLICK_ON_MARK_AS_UNREAD = "click on mark as unread"
            val ACTION_CLICK_BROADCAST_WIZARD = "click on broadcast wizard"
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
                Category.CATEGORY_CHAT_DETAIL,
                Action.ACTION_CLICK_TAB_CHAT_ON_INBOX_CHAT,
                label
        ))
    }

//    #CL6
    fun eventClickChatList(label: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_INBOX_CHAT,
                Category.CATEGORY_CHAT_DETAIL,
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
}