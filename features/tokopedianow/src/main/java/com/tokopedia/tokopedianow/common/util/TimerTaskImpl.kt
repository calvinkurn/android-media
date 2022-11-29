package com.tokopedia.tokopedianow.common.util

import java.util.*

class TimerTaskImpl(private val run : () -> Unit): TimerTask() {
    override fun run() {
        run.invoke()
    }
}
