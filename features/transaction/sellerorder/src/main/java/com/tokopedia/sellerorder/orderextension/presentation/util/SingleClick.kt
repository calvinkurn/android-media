package com.tokopedia.sellerorder.orderextension.presentation.util

import android.os.SystemClock

object SingleClick {

    private var lastClickTime = 0L

    fun doSomethingBeforeTime(interval: Int = 700, block: () -> Unit) {
        if (SystemClock.elapsedRealtime() - lastClickTime < interval) {
            return
        }

        lastClickTime = SystemClock.elapsedRealtime()
        block()
    }
}
