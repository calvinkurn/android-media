package com.tokopedia.inbox.common.config

import com.tokopedia.inboxcommon.RoleType

object InboxConfig {

    @RoleType
    var role: Int = RoleType.BUYER
        private set

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