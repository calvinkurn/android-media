package com.tokopedia.inboxcommon

/**
 * interface to communicate fragment to activity
 * implement this on activity
 */
interface InboxFragmentContainer {
    @RoleType
    val role: Int

    fun clearNotificationCounter()
}