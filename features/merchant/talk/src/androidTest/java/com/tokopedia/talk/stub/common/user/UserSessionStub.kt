package com.tokopedia.talk.stub.common.user

import android.content.Context
import com.tokopedia.user.session.UserSession

class UserSessionStub(
    context: Context
) : UserSession(context) {

    companion object {
        private var instance: UserSession? = null

        fun getInstance(context: Context): UserSession {
            return instance ?: UserSessionStub(context).also {
                instance = it
            }
        }
    }

    var mockUserId = "1430196"
    var mockShopId = "1430195"

    override fun getUserId(): String {
        return mockUserId
    }

    override fun getShopId(): String {
        return mockShopId
    }
}
