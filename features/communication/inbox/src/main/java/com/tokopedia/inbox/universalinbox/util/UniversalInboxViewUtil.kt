package com.tokopedia.inbox.universalinbox.util

import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.unifycomponents.toPx

object UniversalInboxViewUtil {

    val EIGHT_DP = 8.toPx()

    const val WIDGET_RATIO_HALF = 0.5f

    const val ICON_DEFAULT_PERCENTAGE_X_POSITION = 0.9f
    const val ICON_MAX_PERCENTAGE_X_POSITION = 0.75f
    const val ICON_PERCENTAGE_Y_POSITION = -0.25f

    fun getStringCounter(counter: Int): String {
        return when {
            (counter < Int.ONE) -> ""
            (counter > 99) -> "99+"
            else -> counter.toString()
        }
    }
}
