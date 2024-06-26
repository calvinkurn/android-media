package com.tokopedia.productcard.compact.productcard.helper

import java.util.TimerTask

internal class TimerTaskImpl(private val run : () -> Unit): TimerTask() {
    override fun run() {
        run.invoke()
    }
}
