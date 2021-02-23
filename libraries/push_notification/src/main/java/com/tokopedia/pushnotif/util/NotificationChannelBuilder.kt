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
    fun create(context: Context, ringtoneUri: Uri, vibratePattern: LongArray?) {
        val importance = NotificationManager.IMPORTANCE_HIGH
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        val channel = NotificationChannel(Constant.NotificationChannel.GENERAL,
                Constant.NotificationChannel.GENERAL,
                importance)
        val att = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()
        channel.setSound(ringtoneUri, att)
        channel.setShowBadge(true)
        channel.description = Constant.NotificationChannel.GENERAL
        channel.vibrationPattern = vibratePattern
        notificationManager?.createNotificationChannel(channel)
    }

}