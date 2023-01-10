package com.tokopedia.play.util

import android.content.Context
import android.hardware.SensorManager
import android.provider.Settings
import android.view.OrientationEventListener
import com.tokopedia.play.view.contract.PlayOrientationListener
import com.tokopedia.play.view.type.ScreenOrientation

/**
 * Created by jegul on 14/04/20
 */
class PlaySensorOrientationManager(
        private val context: Context,
        private val listener: PlayOrientationListener
) : OrientationEventListener(context, SensorManager.SENSOR_DELAY_NORMAL) {

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

        /**
         * Assuming the default orientation of the device is Portrait
         * Please adjust this number if you find it not appropriate
         */
        if (orientation >= 330 || orientation < 30) {
            requestOrientationChange(ScreenOrientation.Portrait)
        } else if (orientation in 60 until 120) {
            requestOrientationChange(ScreenOrientation.ReversedLandscape)
        } else if (orientation in 150 until 210) {
            /**
             * Range not used
             */
//            requestOrientationChange(ScreenOrientation.ReversedPortrait)
        } else if (orientation in 240 until 300) {
            requestOrientationChange(ScreenOrientation.Landscape)
        }
    }

    private fun requestOrientationChange(newOrientation: ScreenOrientation) {
//        if (currentOrientation == ScreenOrientation.Unknown) {
//            currentOrientation = newOrientation
//            return
//        } else if (newOrientation != currentOrientation) {
//            currentOrientation = newOrientation
//            if (isAutoRotateEnabled) listener.onOrientationChanged(currentOrientation, isTilting = true)
//        }
    }
}
