package com.tokopedia.topads.sdk.utils

import com.tokopedia.topads.sdk.listener.ImpressionListener
import com.tokopedia.topads.sdk.network.HttpMethod
import com.tokopedia.topads.sdk.network.HttpRequest.HttpRequestBuilder
import com.tokopedia.topads.sdk.network.RawHttpRequestExecutor
import com.tokopedia.topads.sdk.utils.ImpressionTaskAlert.Companion.getInstance
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException

/**
 * @author by errysuprayogi on 7/12/17.
 */
class ImpresionTask {
    private var impressionListener: ImpressionListener? = null
    private var taskAlert: ImpressionTaskAlert?
    private var userSession: UserSessionInterface? = null
    private var fileName: String = ""
    private var methodName: String = ""
    private var lineNumber: Int = 0

    init {
        var element = Thread.currentThread().stackTrace[3]
        fileName = element.fileName
        methodName = element.methodName
        lineNumber = element.lineNumber
    }

    constructor(className: String?) {
        taskAlert = getInstance(className!!)
    }

    constructor(className: String?, impressionListener: ImpressionListener?) {
        this.impressionListener = impressionListener
        taskAlert = getInstance(className!!)
    }

    constructor(className: String?, userSession: UserSessionInterface?) {
        this.userSession = userSession
        taskAlert = getInstance(className!!)
    }

    fun execute(url: String?) {
        url?.let {
            GlobalScope.launch(Dispatchers.IO){
                try {
                    if (taskAlert != null) {
                        taskAlert!!.track(url, fileName, methodName, lineNumber)
                    }
                    val request = HttpRequestBuilder()
                            .setBaseUrl(url)
                            .addHeader(KEY_SESSION_ID, if (userSession != null) userSession!!.deviceId else "")
                            .setMethod(HttpMethod.GET)
                            .build()
                    var result = RawHttpRequestExecutor.newInstance(request).executeAsGetRequest()
                    if (impressionListener != null) {
                        if (result != null) {
                            impressionListener!!.onSuccess()
                        } else {
                            impressionListener!!.onFailed()
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                } catch (e: RuntimeException) {
                    e.printStackTrace()
                }
            }
        }
    }

    companion object {
        private const val KEY_SESSION_ID = "Tkpd-SessionID"
    }
}