package com.tokopedia.dev_monitoring_tools

import android.app.Application
import com.google.gson.Gson
import com.tokopedia.dev_monitoring_tools.leakpublisher.TokoLeakPublisher
import leakcanary.LeakCanary
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object DevMonitoringExtension {

    fun initLeakCanary(enable: Boolean = true, application:Application, gson: Gson) {
        LeakCanary.config = LeakCanary.config.copy(dumpHeap = enable)
        CoroutineScope(Dispatchers.IO).launch {
            TokoLeakPublisher.getInstance(application, gson, enable)
        }
    }
}
