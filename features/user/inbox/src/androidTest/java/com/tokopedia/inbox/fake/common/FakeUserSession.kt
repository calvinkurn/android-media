package com.tokopedia.inbox.fake.common

import android.content.Context
import com.tokopedia.user.session.UserSession

class FakeUserSession(context: Context?) : UserSession(context) {

    var fakeHasShop = true
    var fakeShopName = "Toko Rifqi"
    var fakeName = "Rifqi MF"

    override fun hasShop(): Boolean {
        return fakeHasShop
    }

    override fun getShopName(): String {
        return fakeShopName
    }

    override fun getName(): String {
        return fakeName
    }

}