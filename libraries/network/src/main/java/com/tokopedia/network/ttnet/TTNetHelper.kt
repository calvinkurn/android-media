package com.tokopedia.network.ttnet

import android.app.Application
import android.util.Log
import com.bytedance.frameworks.baselib.network.http.cronet.impl.TTNetDetectInfo.TTNetDetectHttpDnsInfo
import com.bytedance.frameworks.baselib.network.http.cronet.impl.TTNetDetectInfo.TTNetDetectHttpGetInfo
import com.bytedance.ttnet.TTNetInit

object TTNetHelper {

    @JvmStatic
    fun initTTNet(app: Application) {
        try {
            val context = app.applicationContext
            CronetDependencyAdapter.inject()
            TTNetInit.setTTNetDepend(TTNetDependencyAdapter(context))
            TTNetInit.tryInitTTNet(
                context,
                app,
                InitTTNetHelper.sApiProcessHook,
                InitTTNetHelper.sMonitorProcessHook,
                null,
                true,
                false
            )
            TTNetInit.preInitCronetKernel()
            Log.d("BYTEIO", "Ip address: ${TTNetDetectHttpGetInfo().ip}")
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
}
