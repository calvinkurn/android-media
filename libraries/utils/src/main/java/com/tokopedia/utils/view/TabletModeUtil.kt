package com.tokopedia.utils.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.pm.ActivityInfo
import android.provider.Settings

object TabletModeUtil {

    const val ACCELEROMETER_ROTATION_ACTIVATED = 1
    const val ACCELEROMETER_ROTATION_DEACTIVATED = 0

    @JvmStatic
    fun Activity.setOrientationToFullSensor() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
    }

    @JvmStatic
    @SuppressLint("SourceLockedOrientationActivity")
    fun Activity.setOrientationToPortrait() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    fun getAccelerometerRotationStatus(contentResolver: ContentResolver): Boolean {
        return Settings.System.getInt(
            contentResolver,
            Settings.System.ACCELEROMETER_ROTATION,
            ACCELEROMETER_ROTATION_DEACTIVATED
        ) == ACCELEROMETER_ROTATION_ACTIVATED
    }

}