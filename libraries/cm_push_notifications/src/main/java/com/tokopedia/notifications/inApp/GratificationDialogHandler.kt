package com.tokopedia.notifications.inApp

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.widget.Toast
import com.tokopedia.notifications.common.CMConstant
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp
import com.tokopedia.promotionstarget.data.notification.NotificationEntryType
import com.tokopedia.promotionstarget.domain.presenter.GratifPopupIngoreType
import com.tokopedia.promotionstarget.domain.presenter.GratificationPresenter
import com.tokopedia.promotionstarget.domain.presenter.GratificationPresenter.GratifPopupCallback
import kotlinx.coroutines.Job
import org.json.JSONObject
import timber.log.Timber
import java.lang.ref.WeakReference

class GratificationDialogHandler(val gratificationPresenter: GratificationPresenter) {

    val TAG = "CmDialogHandler"

    fun showPushDialog(activity: Activity, gratificationId: String) {
        val tempWeakActivity = WeakReference(activity)
        gratificationPresenter.showGratificationInApp(tempWeakActivity, gratificationId, NotificationEntryType.PUSH, null)
    }

    fun showOrganicDialog(currentActivity: WeakReference<Activity>?, data: CMInApp, gratifPopupCallback: GratifPopupCallback): Job? {
        try {
            val customValues = data.customValues
            val json = JSONObject(customValues)
            val gratificationId = json.getString("gratificationId")
            return gratificationPresenter.showGratificationInApp(currentActivity, gratificationId, NotificationEntryType.ORGANIC, gratifPopupCallback)
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
}