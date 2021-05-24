package com.tokopedia.kotlin.extensions.view

import android.content.Context
import android.provider.Settings

/**
 * Created by yfsx on 5/20/21.
 */

fun Context.isDeviceAnimationDisabled() = getAnimationScale(this) == 0f

fun getAnimationScale(context: Context): Float {
    return Settings.Global.getFloat(context.contentResolver,
            Settings.Global.ANIMATOR_DURATION_SCALE, 1.0f)
}