package com.tokopedia.kelontongapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

import com.crashlytics.android.Crashlytics

import io.fabric.sdk.android.Fabric

/**
 * Created by meta on 02/10/18.
 */
class KelontongMainApplication : Application() {

    val NOTIFICATION_CHANNEL_NAME = "mitra_tkpd_notification_channel"
    val NOTIFICATION_CHANNEL_DESC = "mitra_tkpd_notification_channel_desc"

    override fun onCreate() {
        super.onCreate()
        initCrashlytics()
        createNotificationChannel()
    }

    fun initCrashlytics() {
        if (!BuildConfig.DEBUG) {
            Fabric.with(this, Crashlytics())
            Crashlytics.setUserIdentifier(getString(R.string.app_name))
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            mChannel.description = NOTIFICATION_CHANNEL_DESC
            val notificationManager = getSystemService(
                    Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }

    companion object {
        val NOTIFICATION_CHANNEL_ID = "mitra_tkpd_notification_channel_id"
    }
}
