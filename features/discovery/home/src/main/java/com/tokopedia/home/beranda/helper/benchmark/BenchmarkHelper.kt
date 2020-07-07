package com.tokopedia.home.beranda.helper.benchmark

import android.os.Build
import android.os.Trace
import com.tokopedia.config.GlobalConfig

object BenchmarkHelper {
    fun beginSystraceSection(sectionName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2 && GlobalConfig.DEBUG) {
            Trace.beginSection(sectionName)
        }
    }

    fun endSystraceSection() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2 && GlobalConfig.DEBUG) {
            Trace.endSection()
        }
    }
}
