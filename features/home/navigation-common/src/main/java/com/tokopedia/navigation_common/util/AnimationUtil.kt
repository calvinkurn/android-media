package com.tokopedia.navigation_common.util

import android.content.Context
import android.provider.Settings

internal fun Context.getAnimationScale(): Float {
    return Settings.System.getFloat(
        contentResolver,
        Settings.System.ANIMATOR_DURATION_SCALE,
        1.0f
    )
}

internal fun Context.isDeviceAnimationDisabled() = getAnimationScale() == 0f
