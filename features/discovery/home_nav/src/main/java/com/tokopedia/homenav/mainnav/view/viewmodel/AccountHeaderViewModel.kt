package com.tokopedia.homenav.mainnav.view.viewmodel

import com.tokopedia.homenav.base.diffutil.HomeNavTypeFactory
import com.tokopedia.homenav.base.diffutil.HomeNavVisitable
import com.tokopedia.homenav.mainnav.view.adapter.typefactory.MainNavTypeFactory
import com.tokopedia.topads.sdk.domain.model.ImpressHolder

data class AccountHeaderViewModel(
        val id: Int = 0,
        val loginState: Int = 0,
        val userName: String = "",
        val userImage: String = "",
        val badge: String = "",
        val ovoSaldo: String = "",
        val ovoPoint: String = "",
        val saldo: String = "",
        val shopName: String = "",
        val shopId: String = "",
        val shopNotifCount: String = ""
): HomeNavVisitable, ImpressHolder() {
    override fun id(): Any = id

    override fun isDifferent(visitable: HomeNavVisitable): Boolean = id == visitable.id()

    override fun type(factory: HomeNavTypeFactory): Int {
        return (factory as MainNavTypeFactory).type(this)
    }

    companion object {
        const val LOGIN_STATE_LOGIN = 0
        const val LOGIN_STATE_NON_LOGIN = 1
        const val LOGIN_STATE_LOGIN_AS = 2
        const val STICKY_LOGIN_REMINDER_PREF = "sticky_login_reminder.pref"
        const val KEY_USER_NAME = "user_name"
        const val KEY_PROFILE_PICTURE = "profile_picture"
    }
}