package com.tokopedia.appaidl.service

import android.app.Service
import android.content.Intent
import android.os.Bundle
import com.tokopedia.appaidl.AppApi
import com.tokopedia.appaidl.data.componentTargetName

abstract class AidlRemoteService: Service() {

    abstract fun dataShared(tag: String)

    protected fun binder() = object : AppApi.Stub() {
        override fun send(tag: String) {
            dataShared(tag)
        }
    }

    protected fun broadcastResult(tag: String, data: Bundle?) {
        sendBroadcast(Intent().apply {
            `package` = componentTargetName()
            action = tag
            data?.let { putExtras(it) }
        })
    }

}