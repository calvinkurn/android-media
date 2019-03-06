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
        @JvmField
        val REQUEST_CODE_IMMEDIATE = 12135
        private var INAPP_UPDATE = "inappupdate"
        private var INAPP_UPDATE_PREF = "inappupdate_pref"
        private var KEY_INAPP_TYPE = "inapp_type"

        @JvmField
        val REQUEST_CODE_FLEXIBLE = 12136

        private var appUpdateManager: AppUpdateManager? = null

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

        @JvmStatic
        fun checkAndDoFlexibleUpdate(activity: Activity,
                                     onProgress: ((onProgressMessage: String) -> (Unit)),
                                     onError: (() -> (Unit)),
                                     onFinished: (() -> (Unit))) {
            val appContext = activity.applicationContext
            val appUpdateManager = getInstance(appContext)
            if (appUpdateManager == null) {
                onError()
                return
            }
            appUpdateManager.appUpdateInfo.addOnSuccessListener {
                if (it.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                    it.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                    val onProgressUpdating = onProgressUpdating(it.installStatus())
                    if (onProgressUpdating) {
                        val message: String = getProgressMessage(appContext, it.installStatus())
                        onProgress(message)
                        if (it.installStatus() == InstallStatus.DOWNLOADED) {
                            LocalBroadcastManager.getInstance(appContext)
                                .sendBroadcast(Intent(INAPP_UPDATE))
                        }
                    } else {
                        try {
                            val successTriggerUpdate = AppUpdateManagerWrapper.doFlexibleUpdate(activity, it)
                            if (!successTriggerUpdate) {
                                onError()
                            }
                        } catch (e: Exception) {
                            onError()
                        }

                    }
                } else {
                    clearInAppPref(appContext)
                    onError()
                }
                onFinished()
            }.addOnFailureListener {
                onError()
            }
        }

        @JvmStatic
        fun onActivityResult(activity: Activity,
                             requestCode: Int,
                             resultCode: Int) {
            if (requestCode == REQUEST_CODE_FLEXIBLE) {
                if (resultCode == Activity.RESULT_OK){
                    Toast.makeText(activity, activity.getString(R.string.update_install_see_notif), Toast.LENGTH_LONG).show();
                }
                // in else, we do not store the timestamp yet. No requirement for it.
            }
            //request code immediate always forcing
        }

        @JvmStatic
        fun checkAndDoImmediateUpdate(activity: Activity, onError: (() -> (Unit)), onFinished: () -> Unit) {
            val appContext = activity.applicationContext
            val appUpdateManager = getInstance(activity)
            if (appUpdateManager == null) {
                onError()
                return
            }
            appUpdateManager.appUpdateInfo.addOnSuccessListener {
                if (it.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    if (it.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE ||
                        it.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                        // If an in-app update is already running, launch the flow UI.
                        val successTriggerUpdate = AppUpdateManagerWrapper.doImmediateUpdate(activity, it)
                        if (!successTriggerUpdate) {
                            onError()
                        }
                    } else {
                        onError()
                    }
                } else {
                    clearInAppPref(appContext)
                    onError()
                }
            }.addOnFailureListener {
                onError()
            }.addOnCompleteListener {
                onFinished()
            }
        }

        @JvmStatic
        fun doFlexibleUpdate(activity: Activity, appUpdateInfo: AppUpdateInfo): Boolean {
            val appUpdateManager = getInstance(activity.applicationContext) ?: return false
            return try {
                val success = appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.FLEXIBLE, activity, REQUEST_CODE_FLEXIBLE)
                if (success) {
                    setPrefInAppType(activity, AppUpdateType.FLEXIBLE)
                }
                return success
            } catch (e: Exception) {
                false
            }
        }

        @JvmStatic
        fun doImmediateUpdate(activity: Activity, appUpdateInfo: AppUpdateInfo): Boolean {
            val appUpdateManager = getInstance(activity.applicationContext) ?: return false
            return try {
                val success = appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, activity, REQUEST_CODE_IMMEDIATE)
                if (success) {
                    setPrefInAppType(activity, AppUpdateType.IMMEDIATE)
                }
                return success
            } catch (e: Exception) {
                false
            }
        }

        private fun setPrefInAppType(context: Context, appUpdateType: Int) {
            context.getSharedPreferences(INAPP_UPDATE_PREF, Context.MODE_PRIVATE).edit().putInt(KEY_INAPP_TYPE, appUpdateType).apply()
        }

        private fun getPrefInAppType(context: Context): Int =
            context.getSharedPreferences(INAPP_UPDATE_PREF, Context.MODE_PRIVATE).getInt(KEY_INAPP_TYPE, AppUpdateType.IMMEDIATE)

        private fun clearInAppPref(context: Context) {
            context.getSharedPreferences(INAPP_UPDATE_PREF, Context.MODE_PRIVATE).edit().clear().apply()
        }

        /**
         * If flexible update is in progress, or download update is finished, then isInProgress = true,
         */
        @JvmStatic
        fun checkUpdateInFlexibleProgressOrCompleted(activity: Activity, onSuccessGetInfo: ((isInProgress: Boolean) -> (Unit))) {
            val appUpdateManager = getInstance(activity) ?: return
            appUpdateManager.appUpdateInfo.addOnSuccessListener {
                if ((it.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS &&
                        getPrefInAppType(activity) == AppUpdateType.FLEXIBLE) ||
                    it.installStatus() == InstallStatus.DOWNLOADED) {
                    onSuccessGetInfo(true)
                } else {
                    onSuccessGetInfo(false)
                }
            }.addOnFailureListener {
                onSuccessGetInfo(false)
            }
        }

        @JvmStatic
        fun checkUpdateInProgressOrCompleted(activity: Activity) {
            val appUpdateManager = getInstance(activity) ?: return
            appUpdateManager.appUpdateInfo.addOnSuccessListener {
                if (it.installStatus() == InstallStatus.DOWNLOADED) {
                    showSnackBarComplete(activity)
                } else if (it.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS &&
                    getPrefInAppType(activity) == AppUpdateType.IMMEDIATE) {
                    try {
                        appUpdateManager.startUpdateFlowForResult(it, AppUpdateType.IMMEDIATE, /* activity= */ activity, REQUEST_CODE_IMMEDIATE)
                    } catch (e: Exception) {

                    }
                }
            }.addOnFailureListener {
                // no op
            }
        }

        @JvmStatic
        fun showSnackBarComplete(activity: Activity) {
            val appUpdateManager = getInstance(activity) ?: return
            val snackbar = Snackbar.make(
                activity.findViewById<View>(android.R.id.content),
                activity.getString(R.string.completion_update_message),
                Snackbar.LENGTH_INDEFINITE)
            snackbar.setAction(activity.getString(R.string.restart_app)) {
                appUpdateManager.completeUpdate()
                clearInAppPref(activity)
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