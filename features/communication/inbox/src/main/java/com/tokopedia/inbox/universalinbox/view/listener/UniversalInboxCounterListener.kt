package com.tokopedia.inbox.universalinbox.view.listener

interface UniversalInboxCounterListener {
    fun loadWidgetMetaAndCounter(isFirstLoad: Boolean)
    fun onNotificationIconClicked(counter: String)
}
