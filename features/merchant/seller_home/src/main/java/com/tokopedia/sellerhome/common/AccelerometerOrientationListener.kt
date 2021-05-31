package com.tokopedia.sellerhome.common

import android.content.ContentResolver
import android.database.ContentObserver
import android.os.Handler
import android.provider.Settings

class AccelerometerOrientationListener(
        private val contentResolver: ContentResolver,
        private val onAccelerometerOrientationSettingChange: (isEnabled: Boolean) -> Unit
) {

    private val contentObserver: ContentObserver = object : ContentObserver(Handler()) {
        override fun onChange(selfChange: Boolean) {
            val isEnabled = Settings.System.getInt(contentResolver, Settings.System.ACCELEROMETER_ROTATION, 0) == 1
            onAccelerometerOrientationSettingChange(isEnabled)
        }
    }

    fun register() {
        contentResolver.registerContentObserver(Settings.System.getUriFor(Settings.System.ACCELEROMETER_ROTATION), true, contentObserver)
    }

    fun unregister() {
        contentResolver.unregisterContentObserver(contentObserver)
    }
}