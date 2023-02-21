package com.tokopedia.dev_monitoring_tools

import android.app.Application
import com.google.gson.Gson
import com.tokopedia.dev_monitoring_tools.leakpublisher.TokoLeakPublisher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object DevMonitoringExtension {

    fun initLeakCanary(
        enable: Boolean = true,
        isEnableStrictMode: Boolean = false,
        application: Application
    ) {
        if (isEnableStrictMode) {
            CoroutineScope(Dispatchers.Main).launch {
                TokoLeakPublisher.getInstance(application, Gson(), enable, isEnableStrictMode)
            }
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                TokoLeakPublisher.getInstance(application, Gson(), enable, isEnableStrictMode)
            }
        }

    }
}
