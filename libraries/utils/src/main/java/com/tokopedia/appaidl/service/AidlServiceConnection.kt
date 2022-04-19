package com.tokopedia.appaidl.service

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import android.os.RemoteException
import com.tokopedia.appaidl.AppApi

class AidlServiceConnection(
        private val getData: (AppApi?) -> Unit,
) : ServiceConnection {

    private var service: AppApi? = null

    override fun onServiceConnected(componentName: ComponentName?, boundService: IBinder?) {
        try {
            service = AppApi.Stub.asInterface(boundService)
            getData(service)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onServiceDisconnected(componentName: ComponentName?) {
        service = null
    }

}