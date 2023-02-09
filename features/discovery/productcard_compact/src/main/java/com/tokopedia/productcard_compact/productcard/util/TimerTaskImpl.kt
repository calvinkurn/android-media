package com.tokopedia.productcard_compact.productcard.util

import java.util.TimerTask

internal class TimerTaskImpl(private val run : () -> Unit): TimerTask() {
    override fun run() {
        run.invoke()
    }
}
