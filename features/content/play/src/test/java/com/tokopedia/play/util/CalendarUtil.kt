package com.tokopedia.play.util

import java.util.*

/**
 * Created by kenny.hadisaputra on 11/05/22
 */
fun Long.millisFromNow(): Calendar {
    return Calendar.getInstance().apply {
        add(Calendar.MILLISECOND, this@millisFromNow.toInt())
    }
}
