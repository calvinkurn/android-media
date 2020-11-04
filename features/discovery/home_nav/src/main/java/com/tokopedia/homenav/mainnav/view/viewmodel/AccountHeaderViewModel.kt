package com.tokopedia.homenav.mainnav.view.viewmodel

import com.tokopedia.homenav.mainnav.view.adapter.typefactory.MainNavTypeFactory
import com.tokopedia.topads.sdk.domain.model.ImpressHolder

data class AccountHeaderViewModel(
        val id: Int = 0,
        val loginState: Int = 0,
        var userName: String = "",
        val userImage: String = "",
        var badge: String = "",
        var ovoSaldo: String = "",
        var ovoPoint: String = "",
        var saldo: String = "",
        var shopName: String = "",
        val shopId: String = "",
        val shopNotifCount: String = "",
        val shopApplink: String = ""
): MainNavVisitable, ImpressHolder() {
    override fun id(): Any = id

    override fun isContentTheSame(visitable: MainNavVisitable): Boolean {
        return visitable is AccountHeaderViewModel &&
                loginState == visitable.loginState && userName == visitable.userImage &&
                badge == visitable.badge && ovoSaldo == visitable.ovoSaldo &&
                ovoPoint == visitable.ovoPoint && saldo == visitable.saldo &&
                shopName == visitable.shopName && shopId == visitable.shopId &&
                shopNotifCount == visitable.shopNotifCount && shopApplink == visitable.shopApplink
    }

    override fun type(factory: MainNavTypeFactory): Int {
        return factory.type(this)
    }

    companion object {
        const val LOGIN_STATE_LOGIN = 0
        const val LOGIN_STATE_NON_LOGIN = 1
        const val LOGIN_STATE_LOGIN_AS = 2
        const val STICKY_LOGIN_REMINDER_PREF = "sticky_login_reminder.pref"
        const val KEY_USER_NAME = "user_name"
        const val KEY_PROFILE_PICTURE = "profile_picture"
        const val ERROR_TEXT = "Gagal memuat, klik untuk coba lagi"
    }
}