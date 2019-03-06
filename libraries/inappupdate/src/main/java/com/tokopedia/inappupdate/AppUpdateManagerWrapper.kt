package com.tokopedia.inappupdate

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v4.content.LocalBroadcastManager
import android.view.View
import android.widget.Toast
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability

class AppUpdateManagerWrapper {
    companion object {
        private var REQUEST_CODE_IMMEDIATE = 12135
        private var INAPP_UPDATE = "inappupdate"

        @JvmField
        var REQUEST_CODE_FLEXIBLE = 12136

        private var appUpdateManager: AppUpdateManager? = null
        private var appUpdateInfo: AppUpdateInfo? = null

        private fun getInstance(context: Context): AppUpdateManager? {
            if (appUpdateManager == null) {
                appUpdateManager = AppUpdateManagerFactory.create(context.applicationContext)
                appUpdateManager!!.registerListener {
                    if (it.installStatus() == InstallStatus.DOWNLOADED) {
                        LocalBroadcastManager.getInstance(context.applicationContext)
                            .sendBroadcast(Intent(INAPP_UPDATE))
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
            val appContext = context.applicationContext
            val appUpdateManager = getInstance(appContext)
            if (appUpdateManager == null) {
                onSuccessGetInfo(false, false, "")
                return
            }
            appUpdateManager.appUpdateInfo.addOnSuccessListener {
                if (it.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                    it.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                    // check if previously have denied
                    appUpdateInfo = it
                    val onProgressUpdating = onProgressUpdating(it.installStatus())
                    val message: String = getProgressMessage(appContext, it.installStatus())
                    if (onProgressUpdating) {
                        if (it.installStatus() == InstallStatus.DOWNLOADED) {
                            LocalBroadcastManager.getInstance(context.applicationContext)
                                .sendBroadcast(Intent(INAPP_UPDATE))
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

        /**
         * Boolean = true => success get info and allow immediate update
         */
        @JvmStatic
        fun checkImmediateUpdateAllowed(activity: Activity, onSuccessGetInfo: ((allowUpdate: Boolean) -> (Unit))) {
            val appUpdateManager = getInstance(activity)
            if (appUpdateManager == null) {
                onSuccessGetInfo(false)
                return
            }
            appUpdateManager.appUpdateInfo.addOnSuccessListener {
                if (it.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    appUpdateInfo = it
                    val onProgressUpdating = onProgressUpdating(it.installStatus())
                    val message: String = getProgressMessage(activity, it.installStatus())
                    if (onProgressUpdating) {
                        if (it.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                            onSuccessGetInfo(true)
                        } else if (it.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                            // If an in-app update is already running, launch the flow UI.
                            try {
                                appUpdateManager.startUpdateFlowForResult(
                                    appUpdateInfo, AppUpdateType.IMMEDIATE, /* activity= */ activity, REQUEST_CODE_IMMEDIATE)
                            } catch (e: Exception) {

                            }
                        }
                    }
                } else {
                    appUpdateInfo = null
                    onSuccessGetInfo(false)
                }
            }.addOnFailureListener {
                onSuccessGetInfo(false)
            }
        }

        @JvmStatic
        fun doFlexibleUpdate(activity: Activity): Boolean {
            if (appUpdateInfo == null) {
                return false
            }
            val appUpdateManager = getInstance(activity.applicationContext) ?: return false
            var isSuccess = false
            try {
                isSuccess = appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.FLEXIBLE, activity, REQUEST_CODE_FLEXIBLE)
            } catch (e: Exception) {

            }
            return isSuccess
        }

        @JvmStatic
        fun doImmediateUpdate(activity: Activity): Boolean {
            if (appUpdateInfo == null) {
                return false
            }
            val appUpdateManager = getInstance(activity.applicationContext) ?: return false
            var isSuccess = false
            try {
                isSuccess = appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, activity, REQUEST_CODE_IMMEDIATE)
            } catch (e: Exception) {

            }
            return isSuccess
        }

        @JvmStatic
        fun checkUpdateInProgressOrCompleted(activity: Activity, onSuccessGetInfo: ((isInProgress: Boolean) -> (Unit))) {
            val appUpdateManager = getInstance(activity) ?: return
            appUpdateManager.appUpdateInfo.addOnSuccessListener {
                if (it.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE) &&
                    it.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    appUpdateInfo = it
                    Toast.makeText(activity, "In Progress Immediate", Toast.LENGTH_SHORT).show()
                    onSuccessGetInfo(true)
                } else if (it.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                    it.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                    appUpdateInfo = it
                    Toast.makeText(activity, "In Progress Fleexible", Toast.LENGTH_SHORT).show()
                    onSuccessGetInfo(true)
                } else {
                    appUpdateInfo = null
                    onSuccessGetInfo(false)
                }
            }.addOnFailureListener {
                Toast.makeText(activity, "Fail get Info", Toast.LENGTH_SHORT).show()
                onSuccessGetInfo(false)
            }
        }

        @JvmStatic
        fun checkUpdateInProgressOrCompleted(activity: Activity) {
            val appUpdateManager = getInstance(activity) ?: return
            appUpdateManager.appUpdateInfo.addOnSuccessListener {
                if (it.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE) &&
                    it.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    appUpdateInfo = it
                    try {
                        appUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo, AppUpdateType.IMMEDIATE, /* activity= */ activity, REQUEST_CODE_IMMEDIATE)
                    } catch (e: Exception) {

                    }
                } else if (it.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                    it.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                    appUpdateInfo = it
                    if (it.installStatus() == InstallStatus.DOWNLOADED) {
                        showSnackBarComplete(activity)
                    }
                } else {
                    appUpdateInfo = null
                }
            }.addOnFailureListener {
                // no op
            }
        }

        @JvmStatic
        public fun showSnackBarComplete(activity: Activity) {
            val appUpdateManager = getInstance(activity) ?: return
            val snackbar = Snackbar.make(
                activity.findViewById<View>(android.R.id.content),
                activity.getString(R.string.completion_update_message),
                Snackbar.LENGTH_INDEFINITE)
            snackbar.setAction(activity.getString(R.string.restart_app)) {
                appUpdateManager.completeUpdate()
            }
            snackbar.setActionTextColor(ContextCompat.getColor(activity, R.color.snackbar_action_green))
            snackbar.show()
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