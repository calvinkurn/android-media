package com.tokopedia.logger.utils

import timber.log.Timber


enum class Priority {
    P1,
    P2
}

enum class Type {
    WARNING,
    ERROR
}

// Timber.w("P2#PDP_OPEN_DEEPLINK_ERROR#$weblink;errorCode=$errorCode")

fun Timber.sendLog(priority: Priority, tag: String, data: Map<String, String>) {
    val priorityText = when (priority) {
        Priority.P1 -> "P1"
        Priority.P2 -> "P2"
    }

//    val dataBuilder = StringBuilder()
//    val keySet = data.keys
//    keySet.forEachIndexed { index, key ->
//        dataBuilder.append("$key=${data.getValue(key)}")
//        if(index + 1 < keySet.size) {
//            dataBuilder.append("#")
//        }
//    }

    Timber.w(TimberLogException(priority, tag, data))
}

class TimberLogException(priority: Priority, tag: String, data: Map<String, String>): Throwable()