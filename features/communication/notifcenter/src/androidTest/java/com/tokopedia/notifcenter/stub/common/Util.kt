package com.tokopedia.notifcenter.stub.common

private const val UNIX_DIVIDER = 1000L

fun getNewExpiredTime(extendedTime: Long): Long {
    return (System.currentTimeMillis() / UNIX_DIVIDER) + extendedTime
}
