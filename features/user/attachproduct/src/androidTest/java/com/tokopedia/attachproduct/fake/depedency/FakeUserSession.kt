package com.tokopedia.attachproduct.fake.depedency

import android.content.Context
import com.tokopedia.user.session.UserSession
import javax.inject.Inject

class FakeUserSession @Inject constructor(context: Context) : UserSession(context) {

    var fakeHasShop = true
    var fakeShopName = "Toko Attach Product"
    var fakeName = "Attach Product"

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