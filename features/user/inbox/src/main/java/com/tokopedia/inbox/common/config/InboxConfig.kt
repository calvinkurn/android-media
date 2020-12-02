package com.tokopedia.inbox.common.config

import com.tokopedia.inbox.common.InboxFragmentType
import com.tokopedia.inbox.domain.data.notification.InboxCounter
import com.tokopedia.inboxcommon.RoleType

object InboxConfig {

    @RoleType
    var role: Int = RoleType.BUYER
        private set

    @InboxFragmentType
    var initialPage = InboxFragmentType.NOTIFICATION
    var inboxCounter: InboxCounter = InboxCounter()

    interface ConfigListener {
        fun onRoleChanged(@RoleType role: Int)
    }

    private val listener = arrayListOf<ConfigListener?>()

    fun addConfigListener(listener: ConfigListener) {
        this.listener.add(listener)
    }

    fun removeListener(listener: ConfigListener) {
        this.listener.remove(listener)
    }

    fun setRole(@RoleType role: Int?) {
        if (role == null) return
        this.role = role
        listener.forEach {
            it?.onRoleChanged(role)
        }
    }

    fun cleanListener() {
        listener.clear()
    }

}