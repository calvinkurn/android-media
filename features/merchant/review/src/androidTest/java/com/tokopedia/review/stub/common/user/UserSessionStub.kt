package com.tokopedia.review.stub.common.user

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.user.session.UserSession
import javax.inject.Inject

class UserSessionStub @Inject constructor(
    @ApplicationContext
    context: Context
) : UserSession(context) {
    var mockUserId = "1430196"
    var mockShopId = "1430195"

    override fun getUserId(): String {
        return mockUserId
    }

    override fun getShopId(): String {
        return mockShopId
    }
}