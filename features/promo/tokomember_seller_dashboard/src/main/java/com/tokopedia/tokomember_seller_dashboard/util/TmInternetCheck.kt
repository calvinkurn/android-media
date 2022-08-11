package com.tokopedia.tokomember_seller_dashboard.util

import android.content.Context
import com.tokopedia.device.info.DeviceConnectionInfo

object TmInternetCheck {

    fun isConnectedToInternet(context: Context?): Boolean {
        context?.let {
            return DeviceConnectionInfo.isConnectCellular(it) ||
                    DeviceConnectionInfo.isConnectWifi(it)
        }
        return false
    }

}