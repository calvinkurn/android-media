package com.tokopedia.promotionstarget.cmGratification.dialog

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.tokopedia.notifications.inApp.CMInAppManager
import com.tokopedia.notifications.inApp.InAppPopupContract
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp
import com.tokopedia.promotionstarget.cmGratification.ActivityProvider
import com.tokopedia.promotionstarget.data.notification.NotificationEntryType
import com.tokopedia.promotionstarget.domain.presenter.GratifPopupIngoreType
import com.tokopedia.promotionstarget.domain.presenter.GratificationPresenter
import com.tokopedia.promotionstarget.domain.presenter.GratificationPresenter.GratifPopupCallback
import kotlinx.coroutines.Job
import org.json.JSONObject
import java.lang.ref.WeakReference
import java.util.concurrent.ConcurrentHashMap

class GratificationDialogHandler(val gratificationPresenter: GratificationPresenter,
                                 val mapOfGratifJobs: ConcurrentHashMap<Int, Job>,
                                 val mapOfPendingInApp: ConcurrentHashMap<Int, CMInApp>,
                                 val broadcastScreenNames: ArrayList<String>,
                                 val activityProvider: ActivityProvider
) : InAppPopupContract {

    val TAG = "CmDialogHandler"

    fun showPushDialog(activity: Activity, gratificationId: String, screenName:String) {
        val tempWeakActivity = WeakReference(activity)
        gratificationPresenter.showGratificationInApp(tempWeakActivity, gratificationId, NotificationEntryType.PUSH, object : GratificationPresenter.AbstractGratifPopupCallback() {},screenName)
    }

    fun showOrganicDialog(currentActivity: WeakReference<Activity>?, customValues: String, gratifPopupCallback: GratifPopupCallback,screenName: String): Job? {
        try {
            val json = JSONObject(customValues)
            val gratificationId = json.getString("gratificationId")
            return gratificationPresenter.showGratificationInApp(currentActivity, gratificationId, NotificationEntryType.ORGANIC, gratifPopupCallback,screenName)
        } catch (e: Exception) {
            gratifPopupCallback.onExeption(e)
        }
        return null
    }

    fun showIgnoreToast(currentActivity: WeakReference<Activity>?, type: String, @GratifPopupIngoreType reason: Int) {
        val handler = Handler(Looper.getMainLooper())
        handler.post {
            val context = currentActivity?.get()
            if (context != null) {
                val sp1 = context.getSharedPreferences("promo_gratif", Context.MODE_PRIVATE)
                val isDebugGratifChecked = sp1.getBoolean("gratif_debug_toast", false)
                if (isDebugGratifChecked) {
                    val message = "DEBUG - $type - $reason"
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun handleInAppPopup(data: CMInApp, entityHashCode: Int, screenName:String) {
        activityProvider.getActivity()?.get()?.let { currentActivity ->

            /*
            * Check if any pending data is present or not
            * if present -> show the organic pop-up
            * else -> store it
            * */

            if (broadcastScreenNames.contains(currentActivity.javaClass.name) && currentActivity.hashCode() == entityHashCode) {
                val cmInApp = mapOfPendingInApp[entityHashCode]
                if (cmInApp != null) {
                    mapOfPendingInApp.remove(entityHashCode)
                    handleShowOrganic(currentActivity, data, entityHashCode, screenName)
                } else {
                    mapOfPendingInApp[entityHashCode] = data
                }
            } else {
                handleShowOrganic(currentActivity, data, entityHashCode, screenName)
            }
        }
    }

    private fun handleShowOrganic(currentActivity: Activity, data: CMInApp, entityHashCode: Int, screenName: String) {
        val job = showOrganicDialog(WeakReference(currentActivity), data.customValues, object : GratificationPresenter.AbstractGratifPopupCallback() {
            override fun onShow(dialog: DialogInterface) {
                dataConsumed(data)
            }

            override fun onIgnored(reason: Int) {
                if (reason != GratifPopupIngoreType.DIALOG_ALREADY_ACTIVE) {
                    dataConsumed(data)
                }
                showIgnoreToast(null, "organic", reason)
            }

            override fun onExeption(ex: Exception) {
                dataConsumed(data)
                cmInflateException(data)
            }
        },screenName)

        if (job != null)
            mapOfGratifJobs[entityHashCode] = job
    }

    fun executePendingInApp(activity: Activity, screenName: String) {
        if (broadcastScreenNames.contains(activity.javaClass.name)) {
            val cmInApp = mapOfPendingInApp[activity.hashCode()]
            if (cmInApp != null) {
                mapOfPendingInApp.remove(activity.hashCode())
                handleShowOrganic(activity, cmInApp, activity.hashCode(),screenName)
            }
        }
    }

    private fun dataConsumed(data: CMInApp) {
        CMInAppManager.getInstance().cmDataConsumer.dataConsumed(data)
    }

    private fun cmInflateException(data: CMInApp) {
        CMInAppManager.getInstance().onCMInAppInflateException(data)
    }
}