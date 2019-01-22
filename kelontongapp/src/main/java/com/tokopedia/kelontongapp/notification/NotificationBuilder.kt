package com.tokopedia.kelontongapp.notification

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.text.TextUtils
import com.tokopedia.kelontongapp.*

/**
 * Created by meta on 19/10/18.
 */
class NotificationBuilder(context: Context, notificationModel: NotificationModel, notificationId: Int) {

    private val builder: NotificationCompat.Builder

    private val isAllowBell: Boolean
        get() = true

    init {
        this.builder = createNotification(context, notificationModel, notificationId)
    }

    private fun createNotification(context: Context, notificationModel: NotificationModel, notificationId: Int): NotificationCompat.Builder {
        val builder = NotificationCompat.Builder(context, CHANNEL_GENERAL)
        builder.setContentTitle(if (TextUtils.isEmpty(notificationModel.title)) context.resources.getString(R.string.title_general_push_notification) else notificationModel.title)
        builder.setContentText(notificationModel.desc)
        builder.setSmallIcon(setIcon()!!)
        builder.setChannelId(NOTIFICATION_CHANNEL_ID)
        builder.setLargeIcon(BitmapFactory.decodeResource(context.resources, setBigIcon()!!))
        if (allowGroup())
            builder.setGroup(GROUP_GENERAL)
        builder.setContentIntent(createPendingIntent(context, notificationId))
        builder.setAutoCancel(true)

        if (isAllowBell) {
            builder.setSound(setSound())
            builder.setVibrate(setVibrate())
        }
        return builder
    }

    fun build(): Notification {
        return builder.build()
    }

    private fun setIcon(): Int? {
        return R.mipmap.ic_launcher
    }

    private fun setBigIcon(): Int? {
        return R.mipmap.ic_launcher
    }

    private fun setSound(): Uri {
        return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
    }

    private fun setVibrate(): LongArray {
        return longArrayOf(500, 500)
    }

    private fun allowGroup(): Boolean {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT
    }

    private fun createPendingIntent(context: Context, notificationId: Int): PendingIntent {
        val resultPendingIntent: PendingIntent
        val intent = KelontongMainActivity.start(context)
        val bundle = Bundle()
        intent.putExtras(bundle)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            resultPendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            )
        } else {
            resultPendingIntent = PendingIntent.getActivity(
                    context,
                    notificationId,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        return resultPendingIntent
    }
}
