package com.tokopedia.home.beranda.helper.benchmark

import android.os.Trace
import com.tokopedia.config.GlobalConfig

object BenchmarkHelper {
    fun beginSystraceSection(sectionName: String) {
        if (GlobalConfig.DEBUG) {
            Trace.beginSection(sectionName)
        }
    }

    fun endSystraceSection() {
        if (GlobalConfig.DEBUG) {
            Trace.endSection()
        }
    }
}
