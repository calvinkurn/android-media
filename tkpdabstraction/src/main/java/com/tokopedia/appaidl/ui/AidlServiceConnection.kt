package com.tokopedia.appaidl.ui

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import com.tokopedia.appaidl.AppApi

class AidlServiceConnection(
        private var tag: String,
        private val getData: (String, AppApi?) -> Unit,
) : ServiceConnection {

    private var service: AppApi? = null

    override fun onServiceConnected(componentName: ComponentName?, boundService: IBinder?) {
        service = AppApi.Stub.asInterface(boundService)
        getData(tag, service)
    }

    override fun onServiceDisconnected(componentName: ComponentName?) {
        service = null
    }

}