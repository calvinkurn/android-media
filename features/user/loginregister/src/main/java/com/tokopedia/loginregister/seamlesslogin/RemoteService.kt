package com.tokopedia.loginregister.seamlesslogin

import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.loginregister.RemoteApi
import com.tokopedia.loginregister.seamlesslogin.di.DaggerSeamlessLoginComponent
import com.tokopedia.loginregister.seamlesslogin.di.SeamlessLoginComponent
import com.tokopedia.loginregister.seamlesslogin.ui.SeamlessLoginViewModel
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 2020-03-02.
 * Don't move this class to other package to avoid bug
 * seamless login not show up in seller app
 */

class RemoteService : Service(), HasComponent<SeamlessLoginComponent> {

    companion object {
        const val KEY_NAME = "name"
        const val KEY_EMAIL = "email"
        const val KEY_SHOP_AVATAR = "shop_avatar"
        const val KEY_SHOP_NAME = "shop_name"
        const val KEY_PHONE = "phone_no"
        const val KEY_ERROR = "error"

        const val KEYNAME = "key"

        const val MSG_NOT_LOGIN = "not logged in"

        const val PACKAGE_SELLERAPP = "com.tokopedia.sellerapp"
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
            val intent = Intent().apply {
                `package` = PACKAGE_SELLERAPP
                action = taskId
                putExtras(data)
            }
            sendBroadcast(intent)
        } catch (ignored: Exception) {
        }
    }

    fun getKey(taskId: String?) {
        taskId?.run {
            val data = Bundle()
            viewModel.getKey({
                data.apply {
                    putString(KEYNAME, it.key)
                }
                broadCastResult(data, taskId = this@run)
            }, {
                data.apply {
                    putString(KEY_ERROR, it.cause?.message)
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
                    putString(KEY_NAME, userSession.name)
                    putString(KEY_EMAIL, userSession.email)
                    putString(KEY_SHOP_AVATAR, userSession.shopAvatar)
                    putString(KEY_SHOP_NAME, userSession.shopName)
                    putString(KEY_PHONE, userSession.phoneNumber)
                }
            } else {
                data.apply {
                    putString(KEY_ERROR, MSG_NOT_LOGIN)
                }
            }
            broadCastResult(data, taskId = this)
        }
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
