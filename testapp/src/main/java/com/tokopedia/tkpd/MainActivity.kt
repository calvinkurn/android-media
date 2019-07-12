package com.tokopedia.tkpd

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.application.MyApplication
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalPromo
import com.tokopedia.cachemanager.PersistentCacheManager
import com.tokopedia.network.refreshtoken.EncoderDecoder
import com.tokopedia.network.utils.AuthUtil
import com.tokopedia.tkpd.network.DataSource
import com.tokopedia.tkpd.network.LogoutPojo
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.main_testapp.*
import rx.Observable
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_testapp)

        val editTextUser = findViewById<EditText>(R.id.editTextUser)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        editTextUser.setText("fauzanofami.luthfi+01@tokopedia.com")
        editTextPassword.setText("toped12345")
        val loginButton = findViewById<Button>(R.id.loginButton)

        // simplify login process without error handling/verify number/etc
        loginButton.setOnClickListener {
            val userName = editTextUser.text.toString().trim()
            val password = editTextPassword.text.toString()
            KeyboardHandler.hideSoftKeyboard(this)
            val userSession = UserSession(application.applicationContext)
            if (userSession.isLoggedIn) {
                Toast.makeText(this@MainActivity, "already login", Toast.LENGTH_LONG).show()
            } else {
                DataSource.getLoginService(application as MyApplication).getToken(hashMapOf(
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
                        DataSource.getAccountService(application as MyApplication).info.asObservable()
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
                            "hash" to AuthUtil.md5(it.userId.toString() + "~" + DataSource.MOCK_DEVICE_ID),
                            "os_type" to "1",
                            "device_time" to (Date().time / 1000).toString()
                        )
                        DataSource.getWsService(application as MyApplication).makeLogin(map)
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
                        val cache = applicationContext.getSharedPreferences("GCM_STORAGE", Context.MODE_PRIVATE)
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
                            Toast.makeText(this@MainActivity, e?.message, Toast.LENGTH_LONG).show()
                        }

                        override fun onNext(t: Boolean?) {
                            Toast.makeText(this@MainActivity, "success login", Toast.LENGTH_LONG).show()
                        }

                        override fun onCompleted() {

                        }
                    })
            }
        }
        // simplify logout process
        logoutButton.setOnClickListener {
            val userSession = UserSession(application)
            DataSource.getWsLogoutService(application as MyApplication).logout(
                mapOf(
                    "user_id" to userSession.userId.toString(),
                    "device_id" to DataSource.MOCK_DEVICE_ID,
                    "hash" to AuthUtil.md5(userSession.userId.toString() + "~" + DataSource.MOCK_DEVICE_ID),
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
                PersistentCacheManager.instance.delete()
            }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<LogoutPojo> {
                    override fun onError(e: Throwable?) {
                        Toast.makeText(this@MainActivity, e?.message, Toast.LENGTH_LONG).show()
                    }

                    override fun onNext(t: LogoutPojo?) {
                        Toast.makeText(this@MainActivity, "success logout", Toast.LENGTH_LONG).show()
                    }

                    override fun onCompleted() {

                    }
                })

        }

        val button = findViewById<Button>(R.id.button)

        button.setOnClickListener {
            RouteManager.route(this, ApplinkConstInternalMarketplace.SHOP_SETTINGS)
        }
    }

}
