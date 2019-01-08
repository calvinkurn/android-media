package com.tokopedia.travelcalendar.view

import android.annotation.TargetApi
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes

/**
 * Created by nabillasabbaha on 08/01/19.
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public fun getDrawable(context: Context, @DrawableRes resId: Int): Drawable {
    val resources = context.resources
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        resources.getDrawable(resId, context.theme)
    } else {
        resources.getDrawable(resId)
    }
}

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public fun getColor(context: Context, @ColorRes resId: Int): Int {
    val resources = context.resources
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        resources.getColor(resId, context.theme)
    } else {
        resources.getColor(resId)
    }
}