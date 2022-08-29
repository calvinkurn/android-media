package com.tokopedia.product.manage.stub.common

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.user.session.UserSession
import javax.inject.Inject

class UserSessionStub @Inject constructor(
    @ApplicationContext
    context: Context
) : UserSession(context) {
    var mockUserId = "14042"
    var mockShopId = "14045"

    override fun getUserId(): String {
        return mockUserId
    }

    override fun getShopId(): String {
        return mockShopId
    }

    override fun isShopOwner(): Boolean {
        return true
    }

    override fun isLoggedIn(): Boolean {
        return true
    }

    override fun hasShop(): Boolean {
        return true
    }

}