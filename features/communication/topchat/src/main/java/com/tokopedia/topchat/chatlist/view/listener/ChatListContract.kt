package com.tokopedia.topchat.chatlist.view.listener

/**
 * @author : Steven 2019-08-06
 */
interface ChatListContract {
    interface TabFragment {
        fun notifyViewCreated()
        fun loadNotificationCounter()
        fun showSearchOnBoardingTooltip()
        fun closeSearchTooltip()
    }
}
