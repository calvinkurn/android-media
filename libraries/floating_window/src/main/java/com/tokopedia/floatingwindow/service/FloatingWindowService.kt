package com.tokopedia.floatingwindow.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.tokopedia.config.GlobalConfig
import com.tokopedia.floatingwindow.FloatingWindow
import com.tokopedia.floatingwindow.R

/**
 * Created by jegul on 26/11/20
 */
internal class FloatingWindowService : Service() {

    private val processLifecycleObserver = object : LifecycleObserver {

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        fun onApplicationPaused() {
            FloatingWindow.getInstance(applicationContext).removeAllViews()
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    /**
     * Remove the foreground notification and stop the service.
     */
    private fun stopService() {
        FloatingWindow.clearInstance()
        stopForeground(true)
        stopSelf()
    }

    /**
     * Create and show the foreground notification.
     */
    private fun showNotification() {

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val exitIntent = Intent(this, FloatingWindowService::class.java).apply {
            putExtra(INTENT_COMMAND, INTENT_COMMAND_EXIT)
        }

        val exitPendingIntent = PendingIntent.getService(
                this, CODE_EXIT_INTENT, exitIntent, 0
        )

        // From Android O, it's necessary to create a notification channel first.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                with(
                        NotificationChannel(
                                NOTIFICATION_CHANNEL_GENERAL,
                                getString(R.string.notification_channel_floating_window),
                                NotificationManager.IMPORTANCE_DEFAULT
                        )
                ) {
                    enableLights(false)
                    setShowBadge(false)
                    enableVibration(false)
                    setSound(null, null)
                    lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                    manager.createNotificationChannel(this)
                }
            } catch (ignored: Exception) {
                // Ignore exception.
            }
        }

        with(
                NotificationCompat.Builder(
                        this,
                        NOTIFICATION_CHANNEL_GENERAL
                )
        ) {
            setTicker(null)
            setContentTitle("Tokopedia")
            setContentText("Tokopedia is drawing over other apps")
            setAutoCancel(false)
            setOngoing(true)
            setWhen(System.currentTimeMillis())
            setContentIntent(exitPendingIntent)
            setSmallIcon(
                    if (GlobalConfig.isSellerApp()) com.tokopedia.notification.common.R.mipmap.ic_statusbar_notif_seller
                    else com.tokopedia.pushnotif.R.mipmap.ic_statusbar_notif_customer
            )
            priority = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) NotificationManager.IMPORTANCE_DEFAULT else Notification.PRIORITY_DEFAULT
            addAction(
                    NotificationCompat.Action(
                            0,
                            "Exit",
                            exitPendingIntent
                    )
            )
            startForeground(CODE_FOREGROUND_SERVICE, build())
        }

    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        when (intent.getStringExtra(INTENT_COMMAND)) {
            INTENT_COMMAND_START -> {
                FloatingWindow.getInstance(applicationContext).attachView()
            }
            INTENT_COMMAND_EXIT -> {
                stopService()
                return START_NOT_STICKY
            }
        }

        showNotification()

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService()
        ProcessLifecycleOwner.get()
                .lifecycle.removeObserver(processLifecycleObserver)
    }

    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get()
                .lifecycle.addObserver(processLifecycleObserver)
    }

    companion object {
        const val INTENT_COMMAND = "com.tokopedia.floatingwindow.COMMAND"
        const val INTENT_COMMAND_START = "START"
        const val INTENT_COMMAND_EXIT = "EXIT"

        private const val NOTIFICATION_CHANNEL_GENERAL = "floating_window_general"
        private const val CODE_FOREGROUND_SERVICE = 1
        private const val CODE_EXIT_INTENT = 2
    }
}