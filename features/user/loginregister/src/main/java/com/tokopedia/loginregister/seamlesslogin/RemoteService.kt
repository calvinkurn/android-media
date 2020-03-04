package com.tokopedia.loginregister.seamlesslogin

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.loginregister.RemoteApi
import com.tokopedia.loginregister.seamlesslogin.di.SeamlessLoginModule
import com.tokopedia.loginregister.seamlesslogin.di.SeamlessLoginQueryModule
import com.tokopedia.loginregister.seamlesslogin.di.SeamlessLoginUseCaseModule
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 2020-03-02.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class RemoteService : Service() {

    override fun onCreate() {
        super.onCreate()
        DaggerSeamlessLoginComponent.builder()
                .seamlessLoginModule(SeamlessLoginModule())
                .seamlessLoginQueryModule(SeamlessLoginQueryModule())
                .seamlessLoginUseCaseModule(SeamlessLoginUseCaseModule())
                .loginRegisterComponent(((application as BaseMainApplication).baseAppComponent))
                .build()
                .inject(this)
    }

    @Inject lateinit var viewModel: SeamlessLoginViewModel

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    private fun broadCastResult(data: String, taskId: String) {
        val intent = Intent().apply {
            action = taskId
            putExtra("data", data)
        }
        sendBroadcast(intent)
    }

    fun hitDummyApi(taskId: String?) {
        taskId?.run {
            broadCastResult("Ini Result", this)
        }
    }

    private val binder = object : RemoteApi.Stub() {
        override fun getDummyKey(taskId: String?) {
            hitDummyApi(taskId)
        }
    }
}
