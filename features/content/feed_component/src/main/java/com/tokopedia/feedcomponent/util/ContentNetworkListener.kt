package com.tokopedia.feedcomponent.util

import android.annotation.SuppressLint
import android.content.Context
import android.net.wifi.WifiManager

/**
 * @author by yoasfs on 2019-07-11
 */
class ContentNetworkListener constructor(val context: Context) {

    private var wifiManager: WifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: ContentNetworkListener? = null

        fun getInstance(context: Context): ContentNetworkListener {
            if (instance == null) {
                instance = ContentNetworkListener(context)
            }
            return instance!!
        }
    }

    fun isWifiEnabled() : Boolean {
        return wifiManager.isWifiEnabled
    }

}