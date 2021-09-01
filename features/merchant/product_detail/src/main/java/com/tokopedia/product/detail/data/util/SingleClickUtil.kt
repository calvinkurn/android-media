package com.tokopedia.product.detail.data.util

import android.os.SystemClock

/**
 * Created by Yehezkiel on 01/09/21
 */
object SingleClick{

    private var lastClickTime = 0L

    fun doSomethingBeforeTime(block: () -> Unit) {
        if (SystemClock.elapsedRealtime() - lastClickTime < 400) {
            return
        }

        lastClickTime = SystemClock.elapsedRealtime()
        block()
    }
}
