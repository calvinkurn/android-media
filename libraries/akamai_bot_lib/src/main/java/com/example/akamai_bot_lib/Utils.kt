package com.example.akamai_bot_lib

import android.app.Application
import com.akamai.botman.CYFMonitor

fun initAkamaiBotManager(app:Application?){
    app?.let { CYFMonitor.initialize(it) }
}

fun getSensorData() = CYFMonitor.getSensorData()