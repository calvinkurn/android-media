package com.tokopedia.inbox.universalinbox.util

import com.tokopedia.kotlin.extensions.view.ONE

object UniversalInboxViewUtil {

    const val ICON_PERCENTAGE_X_POSITION = 0.85f
    const val ICON_PERCENTAGE_Y_POSITION = -0.45f

    fun getStringCounter(counter: Int): String {
        return when {
            (counter < Int.ONE) -> ""
            (counter > 99) -> "99+"
            else -> counter.toString()
        }
    }
}
