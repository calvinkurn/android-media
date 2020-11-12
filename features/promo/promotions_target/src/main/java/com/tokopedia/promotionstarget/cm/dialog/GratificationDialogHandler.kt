package com.tokopedia.promotionstarget.cm.dialog

import android.app.Activity
import android.content.DialogInterface
import com.tokopedia.notifications.inApp.CMInAppManager
import com.tokopedia.notifications.inApp.InAppPopupContract
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp
import com.tokopedia.promotionstarget.cm.ActivityProvider
import com.tokopedia.promotionstarget.cm.broadcast.PendingData
import com.tokopedia.promotionstarget.data.notification.NotificationEntryType
import com.tokopedia.promotionstarget.domain.presenter.GratifPopupIngoreType
import com.tokopedia.promotionstarget.domain.presenter.GratificationPresenter
import com.tokopedia.promotionstarget.domain.presenter.GratificationPresenter.GratifPopupCallback
import kotlinx.coroutines.Job
import org.json.JSONObject
import java.lang.ref.WeakReference
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class GratificationDialogHandler(val gratificationPresenter: GratificationPresenter,
                                 val mapOfGratifJobs: ConcurrentHashMap<Int, Job>,
                                 val mapOfPendingInApp: ConcurrentHashMap<Int, PendingData>,
                                 val broadcastScreenNames: ArrayList<String>,
                                 val activityProvider: ActivityProvider,
                                 val dialogIsShownMap: WeakHashMap<Activity, Boolean>? = null
) : InAppPopupContract {

    val TAG = "CmDialogHandler"

    fun showPushDialog(activity: Activity, gratificationId: String, screenName: String) {
        val tempWeakActivity = WeakReference(activity)
        gratificationPresenter.showGratificationInApp(tempWeakActivity, gratificationId, NotificationEntryType.PUSH, object : GratificationPresenter.AbstractGratifPopupCallback() {
            override fun onIgnored(reason: Int) {
                super.onIgnored(reason)
                tempWeakActivity.get()?.let {
                    dialogIsShownMap?.remove(it)
                }
            }
        }, screenName)
    }

    fun showOrganicDialog(currentActivity: WeakReference<Activity>?, customValues: String, gratifPopupCallback: GratifPopupCallback, screenName: String): Job? {
        try {
            val json = JSONObject(customValues)
            val gratificationId = json.getString("gratificationId")
            return gratificationPresenter.showGratificationInApp(currentActivity, gratificationId, NotificationEntryType.ORGANIC, gratifPopupCallback, screenName)
        } catch (e: Exception) {
            gratifPopupCallback.onExeption(e)
        }
        return null
    }

    override fun handleInAppPopup(data: CMInApp, entityHashCode: Int, screenName: String) {
        activityProvider.getActivity()?.get()?.let { currentActivity ->

            /*
            * Check if any pending data is present or not
            * if present -> show the organic pop-up
            * else -> store it
            * */

            if (broadcastScreenNames.contains(currentActivity.javaClass.name) && currentActivity.hashCode() == entityHashCode) {
                val pendingData = mapOfPendingInApp[entityHashCode]
                if (pendingData != null && pendingData.isBroadcastCompleted) {
                    mapOfPendingInApp.remove(entityHashCode)
                    handleShowOrganic(currentActivity, data, entityHashCode, screenName)
                } else {
                    mapOfPendingInApp[entityHashCode] = PendingData(false, data)
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
            }

            override fun onExeption(ex: Exception) {
                dataConsumed(data)
                cmInflateException(data)
            }
        }, screenName)

        if (job != null)
            mapOfGratifJobs[entityHashCode] = job
    }

    fun executePendingInApp(activity: Activity, screenName: String): Boolean {
        if (broadcastScreenNames.contains(activity.javaClass.name)) {
            val pendingData = mapOfPendingInApp[activity.hashCode()]
            val cmInApp = pendingData?.cmInApp
            if (cmInApp != null) {
                mapOfPendingInApp.remove(activity.hashCode())
                handleShowOrganic(activity, cmInApp, activity.hashCode(), screenName)
                return true
            } else {
                mapOfPendingInApp[activity.hashCode()] = PendingData(true, null)
            }
        }
        return false
    }

    private fun dataConsumed(data: CMInApp) {
        CMInAppManager.getInstance().cmDataConsumer.dataConsumed(data)
    }

    private fun cmInflateException(data: CMInApp) {
        CMInAppManager.getInstance().onCMInAppInflateException(data)
    }
}