package com.tokopedia.sharedata.service

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.tokopedia.core.model.share.ShareData
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

class ShareBroadcastReceiver: BroadcastReceiver() {

    companion object {
        const val KEY_TYPE = "shareType"
    }

    override fun onReceive(context: Context, intent: Intent) {
        intent.extras?.let {
            for (key in it.keySet()) {
                try {
                    val componentInfo = it.get(key) as ComponentName
                    val packageManager = context.packageManager
                    val appInfo = packageManager.getApplicationInfo(componentInfo.packageName, PackageManager.GET_META_DATA)
                    val appName = packageManager.getApplicationLabel(appInfo)

                    if (intent.extras != null) {
                        val type = it.getString(KEY_TYPE, "")

                        if (type == ShareData.GROUPCHAT_TYPE) {
                            TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                                    "clickShare",
                                    "groupchat room",
                                    "click on choose media to share",
                                    appName.toString()
                            ))
                        }
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    }
}