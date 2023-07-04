package com.tokopedia.kyc_centralized.ui.gotoKyc.utils

import android.content.Context
import com.tokopedia.device.info.DeviceConnectionInfo

fun isConnectionAvailable(context: Context): Boolean {
    return DeviceConnectionInfo.isConnectCellular(context) ||
        DeviceConnectionInfo.isConnectWifi(context) ||
        DeviceConnectionInfo.isConnectEthernet(context)
}
