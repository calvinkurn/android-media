package com.tokopedia.devicefingerprint.submitdevice.service

import android.content.Context

object SubmitDeviceUtil {
    /**
     * Trigger submit device info to backend
     */
    @JvmStatic
    fun scheduleWorker(context: Context, force:Boolean){
        SubmitDeviceWorker.scheduleWorker(context, force)
    }
}