package com.tokopedia.topchat.stub.common

import android.content.Context
import com.tokopedia.user.session.UserSession

class UserSessionStub(context: Context?) : UserSession(context) {

    var hasShop = true

    override fun hasShop(): Boolean {
        return hasShop
    }

}