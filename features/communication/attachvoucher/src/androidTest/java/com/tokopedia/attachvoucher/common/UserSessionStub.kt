package com.tokopedia.attachvoucher.common

import android.content.Context
import com.tokopedia.user.session.UserSession

class UserSessionStub(context: Context?) : UserSession(context) {

    var hasShopStub = true
    var shopNameStub = "Toko Rifqi"
    var nameStub = "Rifqi MF"
    var loggedIn = true

    override fun hasShop(): Boolean {
        return hasShopStub
    }

    override fun getShopName(): String {
        return shopNameStub
    }

    override fun getName(): String {
        return nameStub
    }

    override fun isLoggedIn(): Boolean {
        return loggedIn
    }

}