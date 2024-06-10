package com.tokopedia.analytics.byteio.topads

import android.content.Context
import android.os.SystemClock
import com.bytedance.common.utility.NetworkUtils
import com.tokopedia.config.GlobalConfig
import org.json.JSONObject

fun getSystemBootTime(): String = (System.currentTimeMillis() - SystemClock.elapsedRealtime()).toString()

fun JSONObject.putEnterFrom(enterFrom: String) {
    put(AdsLogConst.Param.ENTER_FROM, enterFrom)
}

fun JSONObject.putChannelName(channelName: String) {
    if (channelName.isNotBlank()) {
        put(AdsLogConst.Param.CHANNEL, channelName)
    }
}

fun JSONObject.putProductName(productName: String) {
    if (GlobalConfig.isAllowDebuggingTools()) {
        put(AdsLogConst.Param.PRODUCT_NAME, productName)
    }
}

fun JSONObject.putTag(tagValue: String) {
    put(AdsLogConst.TAG, tagValue)
}

fun JSONObject.putNetworkType(context: Context) {
    val networkType = NetworkUtils.getNetworkType(context)
    put(AdsLogConst.Param.NT, networkType.value)
}
