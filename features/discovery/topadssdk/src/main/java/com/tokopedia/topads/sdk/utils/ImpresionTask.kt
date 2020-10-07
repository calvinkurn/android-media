package com.tokopedia.topads.sdk.utils

import com.google.gson.reflect.TypeToken
import com.tokopedia.common.network.coroutines.RestRequestInteractor
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.topads.sdk.listener.ImpressionListener
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
    private val restRepository: RestRepository by lazy { RestRequestInteractor.getInstance().restRepository }

    init {
        try {
            var traceElement: StackTraceElement
            var stackTraceElements = Thread.currentThread().stackTrace
            if (stackTraceElements[4].className.equals(TopAdsUrlHitter::class.qualifiedName)) {
                traceElement = stackTraceElements[5]
            } else {
                traceElement = stackTraceElements[4]
            }
            fileName = traceElement.fileName
            methodName = traceElement.methodName
            lineNumber = traceElement.lineNumber
        } catch (e: Exception) {
            e.printStackTrace()
        }
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
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    if (taskAlert != null) {
                        taskAlert!!.track(url, fileName, methodName, lineNumber)
                    }
                    //Request 1
                    val token = object : TypeToken<DataResponse<String>>() {}.type
                    val restRequest = RestRequest.Builder(url, token).build()
                    val result = restRepository.getResponse(restRequest).getData<String>()
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