package com.tokopedia.media.loaderfresco.tracker

import com.bytedance.apm.ApmAgent
import com.optimize.statistics.FrescoMonitor
import com.optimize.statistics.FrescoMonitorConst
import com.optimize.statistics.ImageTraceListener
import org.json.JSONObject
object FrescoLogger {

    fun loggerSlardarFresco() {
        FrescoMonitor.setImageTraceListener(object : ImageTraceListener {
            override fun onImageLoaded(
                isSucceed: Boolean,
                requestId: String?,
                jsonObject: JSONObject?
            ) {
                ApmAgent.monitorCommonLog(FrescoMonitorConst.MONITOR_IMAGE_V2, jsonObject)
            }

            override fun imageNetCallBack(
                duration: Long,
                sendTime: Long,
                url: String?,
                info: com.bytedance.ttnet.http.HttpRequestInfo?,
                e: Throwable?,
                extra: JSONObject?
            ) {
                //no op
            }
        })
    }

}
