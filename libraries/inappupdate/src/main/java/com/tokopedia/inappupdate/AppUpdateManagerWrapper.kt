package com.tokopedia.inappupdate

import android.app.Activity
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.support.v4.content.LocalBroadcastManager
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import java.lang.Exception

class AppUpdateManagerWrapper {
    companion object {
        private var TAG = AppUpdateManagerWrapper::class.java.simpleName
        private var EXTRA_ERROR = "extra_error"
        private var ANDROID_GENERAL_CHANNEL = "ANDROID_GENERAL_CHANNEL"
        private var NOTIFICATION_GROUP = "com.tokopedia"
        private var NOTIFICATION_ID = 7564786

        private var appUpdateManager: AppUpdateManager? = null
        private var appUpdateInfo: AppUpdateInfo? = null

        private fun getInstance(context: Context): AppUpdateManager? {
            if (appUpdateManager == null) {
                appUpdateManager = AppUpdateManagerFactory.create(context.applicationContext)
                appUpdateManager!!.registerListener {
                    if (it.installStatus() == InstallStatus.DOWNLOADED) {
                        LocalBroadcastManager.getInstance(context.applicationContext)
                            .sendBroadcast(Intent(TAG))
                    }
                }
            }
            return appUpdateManager
        }

        /**
         * 1st Boolean = true => success get info and allow flexible update
         * 2nd Boolean = true => download/install status in Progress
         */
        @JvmStatic
        fun checkFlexibleUpdateAllowed(context: Context, onSuccessGetInfo: ((allowUpdate: Boolean,
                                                                             isOnProgress: Boolean,
                                                                             onProgressMessage: String) -> (Unit))) {
            val appUpdateManager = getInstance(context)
            if (appUpdateManager == null) {
                onSuccessGetInfo(false, false, "")
                return
            }
            appUpdateManager.appUpdateInfo.addOnSuccessListener {
                if (it.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                    it.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                    appUpdateInfo = it
                    val onProgressUpdating = onProgressUpdating(it.installStatus())
                    val message: String = getProgressMessage(context, it.installStatus())
                    if (onProgressUpdating) {
                        if (it.installStatus() == InstallStatus.DOWNLOADED) {
                            appUpdateManager.completeUpdate().addOnSuccessListener {
                                LocalBroadcastManager.getInstance(context.applicationContext)
                                    .sendBroadcast(Intent(TAG))
                            }.addOnFailureListener {
                                LocalBroadcastManager.getInstance(context.applicationContext)
                                    .sendBroadcast(Intent(TAG)
                                        .apply { EXTRA_ERROR = it.localizedMessage })
                            }
                        }
                    }
                    onSuccessGetInfo(true, onProgressUpdating, message)
                } else {
                    appUpdateInfo = null
                    onSuccessGetInfo(false, false, "")
                }
            }.addOnFailureListener {
                onSuccessGetInfo(false, false, "")
            }
        }

        @JvmStatic
        fun doFlexibleUpdate(activity: Activity, requestCode: Int): Boolean {
            val appUpdateManager = getInstance(activity.applicationContext) ?: return false
            if (appUpdateInfo == null) {
                return false
            }
            var isSuccess = false
            try {
                isSuccess = appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.FLEXIBLE, activity, requestCode)
                val notification = buildBaseNotification(activity)
                    .build()
                (activity.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                    .notify( NOTIFICATION_ID, notification)
            } catch (e: Exception) {

            }
            return isSuccess
        }

        fun buildBaseNotification(context: Context, contentText:String): NotificationCompat.Builder {
            val title = context.getString(R.string.downloading_update)
            return NotificationCompat.Builder(context, ANDROID_GENERAL_CHANNEL)
                .setProgress(0, 0, true)
                .setContentTitle(title)
                .setContentText(contentText)
                .setSmallIcon(R.drawable.notification_action_background)
                .setAutoCancel(true)
                .setGroup(NOTIFICATION_GROUP)
                .setOnlyAlertOnce(true)
        }

        private fun onProgressUpdating(installStatus: Int): Boolean {
            return when (installStatus) {
                InstallStatus.DOWNLOADING, InstallStatus.INSTALLING,
                InstallStatus.DOWNLOADED, InstallStatus.INSTALLED -> true
                else -> false
            }
        }

        private fun getProgressMessage(context: Context, installStatus: Int): String {
            return when (installStatus) {
                InstallStatus.DOWNLOADING -> context.getString(R.string.downloading_update)
                InstallStatus.INSTALLING -> context.getString(R.string.installing_update)
                InstallStatus.DOWNLOADED -> context.getString(R.string.installing_update)
                InstallStatus.INSTALLED -> context.getString(R.string.installed_update)
                else -> ""
            }
        }
    }

}