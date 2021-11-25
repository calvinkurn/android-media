package com.tokopedia.home_account.stub.domain

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.user.session.UserSession
import javax.inject.Inject

class FakeUserSession @Inject constructor (context: Context) : UserSession(context) {
    override fun isLoggedIn(): Boolean {
        return true
    }

    override fun getPhoneNumber(): String {
        return "08123123123"
    }

    override fun getEmail(): String {
        return "rama.putra@tokopedia.com"
    }
}