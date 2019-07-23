package com.tokopedia.feedcomponent.util

import android.annotation.SuppressLint
import android.content.Context
import android.net.wifi.WifiManager

/**
 * @author by yoasfs on 2019-07-11
 */
class ContentNetworkListener constructor(val context: Context) {

    private var wifiManager: WifiManager

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: ContentNetworkListener? = null

        fun getInstance(context: Context): ContentNetworkListener {
            return instance ?: ContentNetworkListener(context)
        }
    }

    init {
        wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    }

    fun isWifiEnabled() : Boolean {
        return wifiManager.isWifiEnabled
    }

}