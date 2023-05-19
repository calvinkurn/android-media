package com.tokopedia.scp_rewards.common.utils

import android.content.Context
import android.graphics.Color
import android.util.DisplayMetrics

fun dpToPx(context: Context, dp: Int): Float {
    return dp * (context.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT).toFloat()
}

fun parseColor(color:String?,onSuccess:((Int) -> Unit)? = null,onError:((Throwable) -> Unit)?=null){
    try {
        val parsedColor = Color.parseColor(color)
        onSuccess?.invoke(parsedColor)
    }
    catch (err:Exception){
        onError?.invoke(err)
    }
}
