package com.tokopedia.troubleshooter.notification.util

import java.io.Serializable

data class Fourth<out A, out B, out C, out D>(
        val first: A,
        val second: B,
        val third: C,
        val fourth: D
) : Serializable {
    override fun toString(): String = "($first, $second, $third, $fourth)"
}