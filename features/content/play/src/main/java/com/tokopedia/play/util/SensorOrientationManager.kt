package com.tokopedia.play.util

import android.content.Context
import android.hardware.SensorManager
import android.provider.Settings
import android.view.OrientationEventListener
import com.tokopedia.play.view.type.ScreenOrientation

/**
 * Created by jegul on 14/04/20
 */
class SensorOrientationManager(
        private val context: Context,
        private val listener: OrientationListener
) : OrientationEventListener(context, SensorManager.SENSOR_DELAY_NORMAL) {

    companion object {
        /**
         * Assuming the default orientation of the device is Portrait
         */
        private val RANGE_REVERSED_LANDSCAPE = 60..140
        private val RANGE_REVERSED_PORTRAIT = 140..220
        private val RANGE_LANDSCAPE = 220..300

        //TODO("Readjust the range")
    }

    /**
     * Might be worth checking
     * https://stackoverflow.com/questions/6190779/monitor-android-system-settings-values/6191153
     */
    private val isAutoRotateEnabled: Boolean
        get() {
            return try {
                Settings.System.getInt(context.contentResolver,
                        Settings.System.ACCELEROMETER_ROTATION) == 1
            } catch (e: Exception) { false }
        }

    private var currentOrientation = ScreenOrientation.Unknown

    override fun onOrientationChanged(orientation: Int) {
        if (orientation == ORIENTATION_UNKNOWN) return

        val newOrientation: ScreenOrientation = when (orientation) {
            in RANGE_REVERSED_LANDSCAPE -> ScreenOrientation.ReversedLandscape
            in RANGE_REVERSED_PORTRAIT -> ScreenOrientation.ReversedPortrait
            in RANGE_LANDSCAPE -> ScreenOrientation.Landscape
            else -> ScreenOrientation.Portrait
        }

        if (currentOrientation == ScreenOrientation.Unknown) {
            currentOrientation = newOrientation
            return
        } else if (newOrientation != currentOrientation) {
            currentOrientation = newOrientation
            if (isAutoRotateEnabled) listener.onOrientationChanged(currentOrientation)
        }
    }

    interface OrientationListener {
        fun onOrientationChanged(screenOrientation: ScreenOrientation)
    }
}