package com.tokopedia.promotionstarget.cm.dialog

import android.app.Activity
import androidx.annotation.VisibleForTesting
import com.tokopedia.notifications.inApp.CMInAppManager
import com.tokopedia.notifications.inApp.DialogHandlerContract
import com.tokopedia.notifications.inApp.InAppPopupContract
import com.tokopedia.notifications.inApp.ruleEngine.interfaces.DataConsumer
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp
import com.tokopedia.notifications.inApp.viewEngine.CmInAppListener
import com.tokopedia.promotionstarget.cm.ActivityProvider
import com.tokopedia.promotionstarget.cm.broadcast.PendingData
import com.tokopedia.promotionstarget.data.notification.NotificationEntryType
import com.tokopedia.promotionstarget.domain.presenter.GratificationPresenter
import com.tokopedia.promotionstarget.domain.presenter.GratificationPresenter.GratifPopupCallback
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import kotlinx.coroutines.Job
import org.json.JSONObject
import timber.log.Timber
import java.lang.ref.WeakReference
import java.util.*
import java.util.concurrent.ConcurrentHashMap

const val IS_GRATIF_DISABLED = "android_disable_gratif_cm"

class GratificationDialogHandler(val gratificationPresenter: GratificationPresenter,
                                 val mapOfGratifJobs: ConcurrentHashMap<Int, Job>,
                                 val mapOfPendingInApp: ConcurrentHashMap<Int, PendingData>,
                                 val broadcastScreenNames: ArrayList<String>,
                                 val activityProvider: ActivityProvider,
                                 val remoteConfigImpl: FirebaseRemoteConfigImpl?,
                                 val cmInAppListener: CmInAppListener,
                                 dataConsumer: DataConsumer
) : InAppPopupContract, DialogHandlerContract {

    val TAG = "CmDialogHandler"
    val callbackProvider = GratifPopupCallbackProvider(dataConsumer)

    fun showPushDialog(activity: Activity, gratificationId: String, screenName: String) {
        val tempWeakActivity = WeakReference(activity)
        gratificationPresenter.showGratificationInApp(tempWeakActivity, gratificationId, NotificationEntryType.PUSH,
                callbackProvider.getCallbackTypePush(gratificationPresenter, tempWeakActivity), screenName)
    }

    fun showOrganicDialog(currentActivity: WeakReference<Activity>?, customValues: String?, gratifPopupCallback: GratifPopupCallback, screenName: String, inAppId: Long): Job? {
        if (customValues.isNullOrEmpty()) return null
        try {
            val json = JSONObject(customValues)
            val gratificationId = json.getString("gratificationId")
            return gratificationPresenter.showGratificationInApp(
                    weakActivity = currentActivity,
                    gratificationId = gratificationId,
                    notificationEntryType = NotificationEntryType.ORGANIC,
                    gratifPopupCallback = gratifPopupCallback,
                    screenName = screenName, inAppId = inAppId)
        } catch (e: Exception) {
            Timber.e(e)
            gratifPopupCallback.onExeption(e)
        }
        return null
    }

    override fun handleInAppPopup(data: CMInApp, entityHashCode: Int, screenName: String) {
        if (isGratifDisabled()) {
            cmInflateException(data)
        } else {
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
    }

    @VisibleForTesting
    fun handleShowOrganic(currentActivity: Activity, data: CMInApp, entityHashCode: Int, screenName: String) {
        val job = showOrganicDialog(WeakReference(currentActivity), data.customValues,
                callbackProvider.getCallbackTypeOrganic(this, data, cmInAppListener), screenName, data.id)

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


    override fun dataConsumed(data: CMInApp) {
        CMInAppManager.getInstance().cmDataConsumer.dataConsumed(data)
    }

    override fun cmInflateException(data: CMInApp) {
        CMInAppManager.getInstance().onCMInAppInflateException(data)
    }

    fun isGratifDisabled(): Boolean {
        try {
            return remoteConfigImpl?.getBoolean(IS_GRATIF_DISABLED, false) ?: false
        } catch (ex: Exception) {
            Timber.e(ex)
            return false
        }
    }
}