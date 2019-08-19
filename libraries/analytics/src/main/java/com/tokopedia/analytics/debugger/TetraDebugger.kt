package com.tokopedia.analytics.debugger

import android.content.Context
import com.tokopedia.analytics.debugger.data.network.TetraService
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

/**
 * @author okasurya on 2019-08-15.
 * Tetra Debugger is remote analytics debugger to automate data layer testing
 * by validating the data on testing server
 */
class TetraDebugger(private val context: Context) : CoroutineScope {

    val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    /**
     * Initialize the test process, by validating the deviceId of the phone to testing server
     */
    fun init() {
        launchCatchError(coroutineContext, {
            val service = TetraService(context).makeRetrofitService()
            // TODO create request
            val request = TetraService.parse("")
            val response = service.init(request).await()
            // TODO parse response, save to shared preferences
        }, {

        })
    }

    /**
     * send each data layer (currently only support gtm) to testing server
     */
    fun send(userId: String, data: Map<String, Any>) {
        // get status from shared preferences
        // send data to server
    }

    fun cancel() {
        if (!job.isCancelled) {
            job.cancel()
        }

    }
}