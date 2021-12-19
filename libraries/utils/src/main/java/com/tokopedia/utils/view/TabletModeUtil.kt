package com.tokopedia.utils.view

import android.app.Activity
import android.content.ContentResolver
import android.content.pm.ActivityInfo
import android.provider.Settings

object TabletModeUtil {

    @JvmStatic
    fun Activity.setOrientationToLandscape(isEnabled: Boolean) {
        requestedOrientation = if (isEnabled) ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
        else ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    fun getAccelerometerRotationStatus(contentResolver: ContentResolver): Boolean {
        return Settings.System.getInt(
            contentResolver,
            Settings.System.ACCELEROMETER_ROTATION,
            0
        ) == 1
    }

}