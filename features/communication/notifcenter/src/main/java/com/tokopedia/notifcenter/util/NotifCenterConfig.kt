package com.tokopedia.notifcenter.util

import com.tokopedia.notifcenter.data.entity.Notifications
import com.tokopedia.inboxcommon.RoleType

object NotifCenterConfig {

    @RoleType
    var role: Int = RoleType.BUYER
        private set

    var notifications: Notifications = Notifications()
    val inboxCounter get() = notifications.inboxCounter

    interface ConfigListener {
        fun onRoleChanged(@RoleType role: Int)
    }

    private val listener = arrayListOf<ConfigListener?>()

    fun addConfigListener(listener: ConfigListener) {
        NotifCenterConfig.listener.add(listener)
    }

    fun removeListener(listener: ConfigListener) {
        NotifCenterConfig.listener.remove(listener)
    }

    fun setRole(@RoleType role: Int?) {
        if (role == null) return
        NotifCenterConfig.role = role
        listener.forEach {
            it?.onRoleChanged(role)
        }
    }

    fun cleanListener() {
        listener.clear()
    }

}
