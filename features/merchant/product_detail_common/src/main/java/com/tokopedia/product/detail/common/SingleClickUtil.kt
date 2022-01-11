package com.tokopedia.product.detail.common

import android.os.SystemClock

/**
 * Created by Yehezkiel on 01/09/21
 */
object SingleClick{

    private var lastClickTime = 0L

    fun doSomethingBeforeTime(delayInterval: Int = 700, block: () -> Unit) {
        if (SystemClock.elapsedRealtime() - lastClickTime < delayInterval) {
            return
        }

        lastClickTime = SystemClock.elapsedRealtime()
        block()
    }
}
