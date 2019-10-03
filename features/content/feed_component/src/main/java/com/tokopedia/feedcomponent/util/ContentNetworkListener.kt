package com.tokopedia.feedcomponent.util

import android.annotation.SuppressLint
import android.content.Context
import android.net.wifi.WifiManager

/**
 * @author by yoasfs on 2019-07-11
 */
object ContentNetworkListener {
    fun isWifiEnabled(context: Context) : Boolean {
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return wifiManager.isWifiEnabled
    }
}