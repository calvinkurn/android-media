package com.tokopedia.network.ttnet

import android.app.Application
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
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
}
