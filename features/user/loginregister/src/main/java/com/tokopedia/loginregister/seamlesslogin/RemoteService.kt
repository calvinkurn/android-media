package com.tokopedia.loginregister.seamlesslogin

import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.loginregister.RemoteApi
import com.tokopedia.loginregister.common.SeamlessSellerConstant
import com.tokopedia.loginregister.seamlesslogin.di.DaggerSeamlessLoginComponent
import com.tokopedia.loginregister.seamlesslogin.di.SeamlessLoginComponent
import com.tokopedia.loginregister.seamlesslogin.ui.SeamlessLoginViewModel
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.user.session.util.EncoderDecoder
import com.tokopedia.utils.appsignature.AppSignatureUtil
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 2020-03-02.
 * Don't move this class to other package to avoid bug
 * seamless login not show up in seller app
 */

class RemoteService : Service(), HasComponent<SeamlessLoginComponent> {

    companion object {
        const val MSG_NOT_LOGIN = "not logged in"
    }

    override fun onCreate() {
        super.onCreate()
        initInjector()
    }

    private fun initInjector() = component.inject(this)

    @Inject
    lateinit var viewModel: SeamlessLoginViewModel

    @Inject
    lateinit var userSession: UserSessionInterface

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    private fun broadCastResult(data: Bundle, taskId: String) {
        try {
            if (AppSignatureUtil.isSignatureMatch(
                    AppSignatureUtil.getAppSignature(
                        applicationContext,
                        SeamlessSellerConstant.SELLERAPP_PACKAGE
                    ), AppSignatureUtil.TOKO_APP_SIGNATURE
                )
            ) {
                val intent = Intent().apply {
                    `package` = SeamlessSellerConstant.SELLERAPP_PACKAGE
                    action = taskId
                    putExtras(data)
                }
                sendBroadcast(intent)
            }
        } catch (ignored: Exception) {
        }
    }

    fun getKey(taskId: String?) {
        taskId?.run {
            val data = Bundle()
            viewModel.getKey({
                data.apply {
                    putString(SeamlessSellerConstant.KEY_TOKEN, it.key)
                }
                broadCastResult(data, taskId = this@run)
            }, {
                data.apply {
                    putString(SeamlessSellerConstant.KEY_ERROR, it.cause?.message)
                }
                broadCastResult(data, taskId = this)
            })
        }
    }

    fun getUserData(taskId: String?) {
        taskId?.run {
            val data = Bundle()
            if (userSession.isLoggedIn) {
                data.apply {
                    putString(SeamlessSellerConstant.KEY_NAME, encryptData(userSession.name))
                    putString(SeamlessSellerConstant.KEY_EMAIL, encryptData(userSession.email))
                    putString(
                        SeamlessSellerConstant.KEY_SHOP_AVATAR,
                        encryptData(userSession.shopAvatar)
                    )
                    putString(
                        SeamlessSellerConstant.KEY_SHOP_NAME,
                        encryptData(userSession.shopName)
                    )
                }
            } else {
                data.apply {
                    putString(SeamlessSellerConstant.KEY_ERROR, MSG_NOT_LOGIN)
                }
            }
            broadCastResult(data, taskId = this)
        }
    }

    private fun encryptData(data: String): String {
        return EncoderDecoder.Encrypt(data, SeamlessSellerConstant.IV_KEY_SEAMLESS_SELLER)
    }

    private val binder = object : RemoteApi.Stub() {
        override fun getDummyKey(taskId: String?) {
            getKey(taskId)
        }

        override fun getUserProfile(taskId: String?) {
            getUserData(taskId)
        }
    }

    override fun getComponent(): SeamlessLoginComponent = DaggerSeamlessLoginComponent.builder()
        .baseAppComponent((application as BaseMainApplication).baseAppComponent).build()
}
