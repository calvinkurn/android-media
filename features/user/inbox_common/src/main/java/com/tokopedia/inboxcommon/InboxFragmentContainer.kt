package com.tokopedia.inboxcommon

/**
 * interface to communicate fragment to activity
 * implement this on activity
 */
interface InboxFragmentContainer {
    @RoleType
    val role: Int

    fun clearNotificationCounter()
    fun decreaseChatUnreadCounter()
    fun increaseChatUnreadCounter()
    fun refreshNotificationCounter()
    fun decreaseDiscussionUnreadCounter()
    fun decreaseReviewUnreviewedCounter()
    fun hideReviewCounter()
    fun showReviewCounter()
    fun getPageSource(): String
}