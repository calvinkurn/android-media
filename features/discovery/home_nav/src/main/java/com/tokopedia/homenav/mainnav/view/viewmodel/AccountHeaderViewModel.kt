package com.tokopedia.homenav.mainnav.view.viewmodel

import com.tokopedia.homenav.mainnav.view.adapter.typefactory.MainNavTypeFactory
import com.tokopedia.topads.sdk.domain.model.ImpressHolder

data class AccountHeaderViewModel(
        val id: Int = 999,
        var loginState: Int = 0,
        var userName: String = "",
        var userImage: String = "",
        var badge: String = "",
        var ovoSaldo: String = "",
        var ovoPoint: String = "",
        var saldo: String = "",
        var shopName: String = "",
        var shopId: String = "",
        var shopNotifCount: String = "",
        var shopApplink: String = "",
        var isGetUserNameError: Boolean = true,
        var isGetOvoError: Boolean = true,
        var isGetSaldoError: Boolean = true,
        var isGetUserMembershipError: Boolean = true,
        var isGetShopError: Boolean = true,
        var isCacheData: Boolean = false
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

        const val ERROR_TEXT_PROFILE = "Gagal Memuat Profil"
        const val ERROR_TEXT_OVO = "Gagal Memuat Saldo Ovo"
        const val ERROR_TEXT_SHOP = "Gagal Memuat Toko.  %s"
        const val ERROR_TEXT_SHOP_TRY = "Coba Lagi"
    }

    fun copy(): AccountHeaderViewModel {
        return AccountHeaderViewModel(
                id,
                loginState,
                userName,
                userImage,
                badge,
                ovoSaldo,
                ovoPoint,
                saldo,
                shopName,
                shopId,
                shopNotifCount,
                shopApplink
        )
    }

    fun setProfileData(userName: String, userImage: String, loginState: Int) {
        this.userImage = userImage
        this.userName = userName
        this.loginState = loginState
        this.isGetUserNameError = false
    }

    fun setWalletData(ovo: String, point: String) {
        this.ovoSaldo = ovo
        this.ovoPoint = point
        this.isGetOvoError = false
    }

    fun setSaldoData(saldo: String) {
        this.saldo = saldo
        this.isGetSaldoError = false
    }

    fun setUserBadge(badge: String) {
        this.badge = badge
        this.isGetUserMembershipError = false
    }

    fun setUserShopName(shopName: String, shopId: String) {
        this.shopName = shopName
        this.shopId = shopId
        this.isGetShopError = false
    }

}