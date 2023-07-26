@file:Suppress("UNCHECKED_CAST")

package com.tokopedia.scp_rewards.common.utils

import android.content.Context
import android.util.DisplayMetrics

fun dpToPx(context: Context, dp: Int): Float {
    return dp * (context.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT).toFloat()
}
