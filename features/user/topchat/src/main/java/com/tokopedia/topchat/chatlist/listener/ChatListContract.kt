package com.tokopedia.topchat.chatlist.listener

/**
 * @author : Steven 2019-08-06
 */
interface ChatListContract {
    interface Activity {
        fun notifyViewCreated()
        fun loadNotificationCounter()
        fun increaseUserNotificationCounter()
        fun increaseSellerNotificationCounter()
        fun decreaseUserNotificationCounter()
        fun decreaseSellerNotificationCounter()
    }
}