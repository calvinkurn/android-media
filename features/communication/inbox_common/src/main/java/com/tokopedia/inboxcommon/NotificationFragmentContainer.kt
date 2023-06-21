package com.tokopedia.inboxcommon

/**
 * interface to communicate fragment to activity
 * implement this on activity
 */
interface NotificationFragmentContainer {
    @RoleType
    val role: Int

    fun clearNotificationCounter()
    fun refreshNotificationCounter()
    fun getPageSource(): String
}
