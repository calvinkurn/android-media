package com.tokopedia.home.util

import android.content.Context
import com.tokopedia.core.analytics.ConnectivityUtils
import com.tokopedia.core.analytics.ConnectivityUtils.getConnectionType
import com.tokopedia.core.analytics.ConnectivityUtils.isConnected


object ConnectionUtils{
    fun isWifiConnected(context: Context): Boolean {
        return isConnected(context) && getConnectionType(context) == ConnectivityUtils.CONN_WIFI
    }
}