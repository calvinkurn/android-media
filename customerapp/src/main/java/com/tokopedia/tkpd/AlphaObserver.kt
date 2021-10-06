package com.tokopedia.tkpd

import android.app.Activity
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat

const val CHANNEL_ID = "alpha"
const val NOTIFICATION_ID = 123 shr 5

class AlphaObserver : Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
        setWindowAlpha(activity)
        showBanner(activity)
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }

    private fun showBanner(context: Context) {
        val mNotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mNotificationManager.createNotificationChannel(
                NotificationChannel(
                    CHANNEL_ID,
                    context.getString(R.string.alpha_notification_category),
                    NotificationManager.IMPORTANCE_LOW
                )
            )
        }

        val remoteView = RemoteViews(context.packageName, R.layout.alpha_notification_layout)
        remoteView.setTextViewText(R.id.mynotifyexpnd, context.getString(R.string.tokopedia_alpha))
        val mBuilder =
            NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_icon_alert)
                .setCustomContentView(remoteView)
                .setColor(ContextCompat.getColor(context, android.R.color.holo_red_dark))

        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build())
    }

    private fun setWindowAlpha(activity: Activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            val window = activity.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = ContextCompat.getColor(activity, android.R.color.holo_red_dark)
        }
    }

}