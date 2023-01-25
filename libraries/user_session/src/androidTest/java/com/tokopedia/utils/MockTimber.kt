package com.tokopedia.utils

import timber.log.Timber

class MockTimber : Timber.Tree() {
    val logs = mutableListOf<Log>()

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        logs.add(Log(priority, tag, message, t))
    }

    fun lastLogMessage(): String {
        return logs.last().message
    }

    data class Log(val priority: Int, val tag: String?, val message: String, val t: Throwable?)
}
