package com.tokopedia.dynamicfeatures.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class DFBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        try {
            context?.startService(Intent(context, DFDownloadService::class.java))
        } catch (ignored:Exception) { }
    }
}
