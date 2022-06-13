package com.tokopedia.shop_widget.common.util

import java.util.*

object ServerTimeOffsetUtil {
    private const val ONE_SECOND: Long = 1000

    private fun getServerTimeOffset(serverTimeMillisecond: Long): Long {
        val localDate = Date()
        val localTimeMillis = localDate.time
        return serverTimeMillisecond - localTimeMillis
    }

    fun getServerTimeOffsetFromUnix(serverTimeUnix: Long): Long {
        val serverTimemillis = serverTimeUnix * ONE_SECOND
        return getServerTimeOffset(serverTimemillis)
    }
}