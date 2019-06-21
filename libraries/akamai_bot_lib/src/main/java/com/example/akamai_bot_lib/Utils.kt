package com.example.akamai_bot_lib

import android.app.Application
import android.os.Build
import com.akamai.botman.CYFMonitor
import com.tokopedia.config.GlobalConfig

fun initAkamaiBotManager(app:Application?){
    app?.let { CYFMonitor.initialize(it) }
}

fun getSensorData() = CYFMonitor.getSensorData()

private val userAgentFormat = "TkpdConsumer/%s (%s;)"

fun getUserAgent(): String {
    return String.format(userAgentFormat, GlobalConfig.VERSION_NAME, "Android " + Build.VERSION.RELEASE)
}