package com.tokopedia.inboxcommon

/**
 * interface to communicate activity to fragment
 * implement this on fragment
 */
interface InboxFragment {
    fun onRoleChanged(@RoleType role: Int)
    fun onPageClickedAgain()
}