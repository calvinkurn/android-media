package com.tokopedia.appaidl.ui

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import com.tokopedia.appaidl.AppApi

class ServiceView(
        private var appName: String,
        private val getData: (String, AppApi?) -> Unit,
) : ServiceConnection {

    private var service: AppApi? = null

    override fun onServiceConnected(componentName: ComponentName?, boundService: IBinder?) {
        service = AppApi.Stub.asInterface(boundService)
        Log.d("AppApi", "onServiceConnected")
        getData(appName, service)
    }

    override fun onServiceDisconnected(componentName: ComponentName?) {
        service = null
        Log.d("AppApi", "onServiceDisconnected")
    }

}