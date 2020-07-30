package com.tokopedia.home.beranda.helper.benchmark

import android.os.Build
import android.os.Trace

object BenchmarkHelper {
    fun beginSystraceSection(sectionName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            Trace.beginSection(sectionName)
        }
    }

    fun endSystraceSection() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            Trace.endSection()
        }
    }
}