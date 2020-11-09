package com.tokopedia.inboxcommon

interface InboxFragment {
    fun onRoleChanged(@RoleType role: Int)
    fun onPageClickedAgain()
}