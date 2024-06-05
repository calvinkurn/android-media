package com.tokopedia.dev_monitoring_tools

import leakcanary.LeakCanary

object DevMonitoringExtension {

    fun initLeakCanary(enable: Boolean = false) {
        LeakCanary.config = LeakCanary.config.copy(dumpHeap = enable)
    }
}
