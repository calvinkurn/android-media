package com.tokopedia.pushnotif.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import com.tokopedia.pushnotif.data.constant.Constant

object NotificationChannelBuilder {

    @JvmStatic
    @RequiresApi(api = Build.VERSION_CODES.O)
    fun create(context: Context, ringtoneUri: Uri?, vibratePattern: LongArray?) {
        val notificationManager = context.getSystemService(NotificationManager::class.java)

        NotificationChannel(Constant.NotificationChannel.GENERAL,
                Constant.NotificationChannel.GENERAL,
                NotificationManager.IMPORTANCE_HIGH
        ).apply {
            ringtoneUri?.run {
                setSound(this, AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .build())
            }
            description = Constant.NotificationChannel.GENERAL
            vibrationPattern = vibratePattern

            setShowBadge(true)
        }.also {
            notificationManager?.createNotificationChannel(it)
        }
    }

}