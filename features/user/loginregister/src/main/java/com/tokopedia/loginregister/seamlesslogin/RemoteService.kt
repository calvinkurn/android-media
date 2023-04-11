package com.tokopedia.loginregister.seamlesslogin

import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.loginregister.RemoteApi
import com.tokopedia.loginregister.common.di.DaggerLoginRegisterComponent
import com.tokopedia.loginregister.seamlesslogin.di.DaggerSeamlessLoginComponent
import com.tokopedia.loginregister.seamlesslogin.di.SeamlessLoginModule
import com.tokopedia.loginregister.seamlesslogin.di.SeamlessLoginQueryModule
import com.tokopedia.loginregister.seamlesslogin.di.SeamlessLoginUseCaseModule
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 2020-03-02.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class RemoteService : Service() {

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
        DaggerSeamlessLoginComponent.builder()
                .seamlessLoginModule(SeamlessLoginModule(applicationContext))
                .seamlessLoginQueryModule(SeamlessLoginQueryModule())
                .seamlessLoginUseCaseModule(SeamlessLoginUseCaseModule())
                .loginRegisterComponent(DaggerLoginRegisterComponent.builder().baseAppComponent((application as BaseMainApplication).baseAppComponent).build())
                .build()
                .inject(this)
    }

    @Inject lateinit var viewModel: SeamlessLoginViewModel
    @Inject lateinit var userSession: UserSessionInterface

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
        } catch (ignored: Exception) {}
    }

    fun getKey(taskId: String?){
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

    fun getUserData(taskId: String?){
        taskId?.run {
            val data = Bundle()
            if(userSession.isLoggedIn) {
                data.apply {
                    putString(KEY_NAME, userSession.name)
                    putString(KEY_EMAIL, userSession.email)
                    putString(KEY_SHOP_AVATAR, userSession.shopAvatar)
                    putString(KEY_SHOP_NAME, userSession.shopName)
                    putString(KEY_PHONE, userSession.phoneNumber)
                }
            }else {
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
}
