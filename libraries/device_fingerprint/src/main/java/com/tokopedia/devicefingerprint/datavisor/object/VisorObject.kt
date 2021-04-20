package com.tokopedia.devicefingerprint.datavisor.`object`

import com.tokopedia.encryption.utils.Utils.decodeDecimalToText

object VisorObject {
    object Key {
        val APP_KEY = decodeDecimalToText(intArrayOf(99, 111, 109, 46, 116, 111, 107, 111, 112, 101, 100, 105, 97, 46, 84, 111, 107, 111, 112, 101, 100, 105, 97, 45, 99, 66, 110, 53, 86, 72, 90, 118))
        val APP_SECRET = decodeDecimalToText(intArrayOf(54, 88, 70, 52, 103, 108, 87, 81, 97, 79, 78, 55, 85, 117, 103, 79, 99, 52, 51, 121, 110, 84, 80, 121, 122, 88, 49, 76, 81, 104, 55, 108))
        val APP_DOMAIN = "tokopedia.gw-dv.net"
    }
}