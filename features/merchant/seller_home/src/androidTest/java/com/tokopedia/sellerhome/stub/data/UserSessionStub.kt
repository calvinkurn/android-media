package com.tokopedia.sellerhome.stub.data

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.user.session.UserSession
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 02/12/21.
 */

class UserSessionStub @Inject constructor(
    @ApplicationContext
    context: Context
) : UserSession(context) {

    private var mockShopName = "Sojol Fishing Shop"
    private var mockUserId = "1430196"
    private var mockShopId = "1430195"
    private var mockSasShop = true
    private var mockIsLoggedIn = true


    override fun getUserId(): String {
        return mockUserId
    }

    override fun getShopId(): String {
        return mockShopId
    }

    override fun hasShop(): Boolean {
        return mockSasShop
    }

    override fun isLoggedIn(): Boolean {
        return mockIsLoggedIn
    }

    override fun getShopName(): String {
        return mockShopName
    }
}