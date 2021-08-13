package com.tokopedia.promotionstarget.cm.broadcast

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp
import com.tokopedia.promotionstarget.cm.dialog.GratificationDialogHandler
import java.lang.ref.WeakReference

const val DISCO_IS_NATIVE = "DISCO_IS_NATIVE"
const val DISCO_INTENT_FILTER = "DISCO_ACTIVITY_SELECTION"

class BroadcastHandler(val dialogHandler: GratificationDialogHandler) {

    private val broadcastReceiverMap = HashMap<WeakReference<Activity>, WeakReference<BroadcastReceiver>>()

    fun processActivity(activity: Activity): BroadcastReceiver {
        val receiver: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val bundle = intent.extras
                if (bundle != null) {
                    val isNative = bundle.getBoolean(DISCO_IS_NATIVE)
                    if (isNative) {
                        dialogHandler.executePendingInApp(activity, activity.javaClass.name)
                    }
                }
            }
        }

        LocalBroadcastManager.getInstance(activity).registerReceiver(receiver, IntentFilter(DISCO_INTENT_FILTER))

        broadcastReceiverMap[WeakReference(activity)] = WeakReference(receiver)
        return receiver
    }

    fun onActivityStop(activity: Activity) {
        unRegisterBroadcastReceiver(activity)
    }

    private fun unRegisterBroadcastReceiver(activity: Activity) {
        val set: Set<WeakReference<Activity>> = broadcastReceiverMap.keys
        var finalWeakActivity: WeakReference<Activity>? = null
        for (weakReference in set) {
            if (weakReference.get() === activity) {
                finalWeakActivity = weakReference
                break
            }
        }
        if (finalWeakActivity != null) {
            val weakReceiver = broadcastReceiverMap[finalWeakActivity]
            if (weakReceiver != null) {
                val receiver = weakReceiver.get()
                if (receiver != null) {
                    LocalBroadcastManager.getInstance(activity).unregisterReceiver(receiver)
                }
                broadcastReceiverMap.remove(finalWeakActivity)
            }
        }
    }
}

data class PendingData(var isBroadcastCompleted: Boolean, val cmInApp: CMInApp?)