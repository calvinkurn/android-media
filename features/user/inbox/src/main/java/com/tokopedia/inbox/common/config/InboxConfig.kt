package com.tokopedia.inbox.common.config

import com.tokopedia.inbox.common.RoleType

object InboxConfig {

    @RoleType
    var role: Int = RoleType.BUYER
        private set

    private val listener = arrayListOf<ConfigListener?>()

    interface ConfigListener {
        fun onRoleChanged(@RoleType role: Int)
    }

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

}