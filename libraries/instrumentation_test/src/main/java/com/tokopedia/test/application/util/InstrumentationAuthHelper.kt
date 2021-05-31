package com.tokopedia.test.application.util

import android.app.Application
import android.content.Context
import androidx.test.espresso.idling.CountingIdlingResource
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.authentication.AuthHelper
import com.tokopedia.network.refreshtoken.EncoderDecoder
import com.tokopedia.test.application.environment.InstrumentationTestApp
import com.tokopedia.test.application.environment.network.DataSource
import com.tokopedia.user.session.UserSession
import rx.Observable
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*

object InstrumentationAuthHelper {

    fun loginInstrumentationTestUser1() {
        userSession {
            userId = "108956738"
            email = "erick.samuel+testingtokenandroid1@tokopedia.com"
            accessTokenBearer = "ghSZU8GxoVSK3qkEqgFUrlHt3pFSS+Xtmb5peuCDaca/R0LwyqhTqwTJVcupIX78E5xicw3oliW9AdyRWr4Apg=="
        }
    }

    fun loginInstrumentationTestUser2() {
        userSession {
            userId = "108956738"
            email = "erick.samuel+testingtokenandroid1@tokopedia.com"
            accessTokenBearer = "kdxPYUwtF5yYMOuwZFxnFqFZea7GUpoX6m1eL1IGJ1pwB3crhQCTvKdMoYV6wIpiHgE5Xlghd0WAKPXW+yMp5w=="
            shopId = "0"
            setIsShopOwner(true)
        }
    }

    fun loginInstrumentationTestTopAdsUser() {
        userSession {
            userId = "24095631"
            email = "jaka.pitana+akuntestprod@tokopedia.com"
            accessTokenBearer = "SKHPkiHiIE75+ZfNvZxlob5LCwzqSrTQ6gCh9ZTK/3sQzeQ2hqB56z9vwAG2Ky0W"
        }
    }

    fun clearUserSession() {
        try {
            val userSession = UserSession(InstrumentationRegistry.getInstrumentation().targetContext)
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
            context: Context = InstrumentationRegistry.getInstrumentation().targetContext,
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

    private fun userSessionWithModifier(
            context: Context = InstrumentationRegistry.getInstrumentation().targetContext,
            modifyUserSession: ((UserSession) -> Unit)? = null,
            action: UserSession.() -> Unit
    ) {
        try {
            val userSession = UserSession(context)
            modifyUserSession?.invoke(userSession)
            userSession.setIsLogin(true)
            userSession.action()
        }
        catch (throwable: Throwable) {
            throwable.printStackTrace()
        }
    }



    private var UserSession.accessTokenBearer: String
        get() = accessToken
        set(bearerToken) = setToken(bearerToken, "Bearer")

    fun loginToAnUser(
            application: Application,
            idlingResource: CountingIdlingResource? = null,
            userName: String = "fauzanofami.luthfi+01@tokopedia.com",
            password: String = "toped12345"
    ) {
        idlingResource?.increment()
        val userSession = UserSession(application)

        DataSource.getLoginService(application as InstrumentationTestApp).getToken(hashMapOf(
                "username" to userName,
                "password" to password,
                "grant_type" to "password"))
                .map { tokenModel ->
                    if (tokenModel == null || tokenModel.accessToken.isNullOrEmpty()) {
                        throw (RuntimeException("Error user pass"))
                    } else {
                        tokenModel
                    }
                }
                .doOnNext {
                    userSession.setToken(
                            it.accessToken,
                            it.tokenType,
                            EncoderDecoder.Encrypt(it.refreshToken,
                                    userSession.refreshTokenIV))
                }
                .flatMap {
                    DataSource.getAccountService(application as InstrumentationTestApp).info.asObservable()
                }
                .map { userInfoData ->
                    if (userInfoData == null || userInfoData.userId.toString().isEmpty()) {
                        throw (RuntimeException("Error get user data"))
                    } else {
                        userInfoData
                    }
                }
                .doOnNext {
                    if (!userSession.isLoggedIn) {
                        userSession.setTempUserId(it.userId.toString())
                        userSession.tempPhoneNumber = it.phone
                        userSession.setTempLoginName(it.fullName)
                        userSession.setTempLoginEmail(it.email)
                    }
                    userSession.setHasPassword(it.isCreatedPassword)
                    userSession.profilePicture = it.profilePicture
                    userSession.setIsMSISDNVerified(it.isPhoneVerified)
                }
                .flatMap {
                    val map = mapOf(
                            "user_id" to it.userId.toString(),
                            "device_id" to DataSource.MOCK_DEVICE_ID,
                            "hash" to InstrumentationAuthHelper.getHash(it.userId.toString()),
                            "os_type" to "1",
                            "device_time" to (Date().time / 1000).toString()
                    )
                    DataSource.getWsService(application as InstrumentationTestApp).makeLogin(map)
                }
                .map { makeLoginPojo ->
                    if (makeLoginPojo == null || makeLoginPojo.data == null) {
                        throw (RuntimeException("Error get make login"))
                    } else {
                        makeLoginPojo
                    }
                }
                .map { makeLoginPojo ->
                    makeLoginPojo.data
                }
                .doOnNext { makeLoginPojo ->
                    // bypass sec pojo
                    userSession.setLoginSession(true,
                            makeLoginPojo.userId.toString(),
                            makeLoginPojo.fullName,
                            makeLoginPojo.shopId.toString(),
                            true,
                            makeLoginPojo.shopName,
                            userSession.tempEmail,
                            makeLoginPojo.shopIsGold == 1,
                            userSession.tempPhoneNumber)
                    val cache = application.applicationContext.getSharedPreferences("GCM_STORAGE", Context.MODE_PRIVATE)
                    cache.edit().putString("gcm_id", DataSource.MOCK_DEVICE_ID).apply()
                    if (makeLoginPojo.securityPojo.allowLogin != 1) {
                        throw (RuntimeException("security Pojo fail"))
                    }
                }
                .flatMap {
                    Observable.just(true)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Boolean> {
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: Boolean?) {
                    }

                    override fun onCompleted() {
                        if (idlingResource?.isIdleNow == false) {
                            idlingResource.decrement()
                        }
                    }
                })
    }

    fun getHash(userId: String): String {
        return AuthHelper.getMD5Hash(userId + "~" + DataSource.MOCK_DEVICE_ID)
    }
}
