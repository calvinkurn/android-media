package com.tokopedia.test.application.util

import android.app.Application
import android.content.Context
import android.widget.Toast
import com.tokopedia.authentication.AuthHelper
import com.tokopedia.network.refreshtoken.EncoderDecoder
import com.tokopedia.test.application.environment.InstrumentationTestApp
import com.tokopedia.test.application.environment.network.DataSource
import com.tokopedia.test.application.environment.network.LogoutPojo
import com.tokopedia.user.session.UserSession
import rx.Observable
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*

object InstrumentationAuthHelper {
    fun loginToAnUser(application: Application) {
        val userSession = UserSession(application)

        val userName = "fauzanofami.luthfi+01@tokopedia.com"
        val password = "toped12345"
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
                        Toast.makeText(application, e?.message, Toast.LENGTH_LONG).show()
                    }

                    override fun onNext(t: Boolean?) {
                        Toast.makeText(application, "success login", Toast.LENGTH_LONG).show()
                    }

                    override fun onCompleted() {

                    }
                })
    }

    fun loginInstrumentationTestUser1(context: Context) {
        val accessToken = "ghSZU8GxoVSK3qkEqgFUrlHt3pFSS+Xtmb5peuCDaca/R0LwyqhTqwTJVcupIX78E5xicw3oliW9AdyRWr4Apg=="
        loginWithAccessToken(context, accessToken)
    }

    private fun loginWithAccessToken(context: Context, accessToken: String) {
        try {
            val userSession = UserSession(context)
            userSession.userId = "108956738"
            userSession.email = "erick.samuel+testingtokenandroid1@tokopedia.com"
            userSession.setToken(accessToken, "Bearer")
            userSession.setIsLogin(true)
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        }
    }

    fun logoutTestUser(context: Context) {
        try {
            val userSession = UserSession(context)
            userSession.logoutSession()
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        }
    }

    fun logoutFromAnUser(application: Application) {
        val userSession = UserSession(application)
        DataSource.getWsLogoutService(application as InstrumentationTestApp).logout(
                mapOf(
                        "user_id" to userSession.userId.toString(),
                        "device_id" to DataSource.MOCK_DEVICE_ID,
                        "hash" to AuthHelper.getMD5Hash(userSession.userId.toString() + "~" + DataSource.MOCK_DEVICE_ID),
                        "os_type" to "1",
                        "device_time" to (Date().time / 1000).toString()
                )
        ).map { logoutPojo ->
            if (logoutPojo?.data == null) {
                throw (RuntimeException("Error logout"))
            } else {
                logoutPojo
            }
        }.doOnNext {
            userSession.logoutSession()
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<LogoutPojo> {
                    override fun onError(e: Throwable?) {
                        Toast.makeText(application, e?.message, Toast.LENGTH_LONG).show()
                    }

                    override fun onNext(t: LogoutPojo?) {
                        Toast.makeText(application, "success logout", Toast.LENGTH_LONG).show()
                    }

                    override fun onCompleted() {

                    }
                })
    }



    fun getHash(userId: String): String {
        return AuthHelper.getMD5Hash(userId + "~" + DataSource.MOCK_DEVICE_ID)
    }
}
