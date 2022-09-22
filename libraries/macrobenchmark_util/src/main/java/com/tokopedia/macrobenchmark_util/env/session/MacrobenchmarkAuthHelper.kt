package com.tokopedia.macrobenchmark_util.env.session

import android.content.Context
import com.tokopedia.user.session.UserSession

object MacrobenchmarkAuthHelper {
    private var UserSession.accessTokenBearer: String
        get() = accessToken
        set(bearerToken) = setToken(bearerToken, "Bearer")

    fun loginInstrumentationTestUser1(context: Context) {
        userSession(context) {
            userId = "108956738"
            email = "erick.samuel+testingtokenandroid1@tokopedia.com"
            accessTokenBearer =
                "ghSZU8GxoVSK3qkEqgFUrlHt3pFSS+Xtmb5peuCDaca/R0LwyqhTqwTJVcupIX78E5xicw3oliW9AdyRWr4Apg=="
        }
    }

    fun clearUserSession(context: Context) {
        try {
            val userSession = UserSession(context)
            userSession.userId = ""
            userSession.email = ""
            userSession.accessTokenBearer = ""
            userSession.setIsLogin(false)
        }
        catch (throwable: Throwable) {
            throwable.printStackTrace()
        }
    }

    fun userSession(
        context: Context,
        action: UserSession.() -> Unit
    ) {
        try {
            val userSession = UserSession(context)
            userSession.setIsLogin(true)
            userSession.action()
        }
        catch (throwable: Throwable) {
            throwable.printStackTrace()
        }
    }
}