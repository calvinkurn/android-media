package com.tokopedia.analytics.debugger

import android.content.Context
import com.tokopedia.analytics.debugger.data.mapper.TetraMapper
import com.tokopedia.analytics.debugger.data.network.TetraService
import com.tokopedia.analytics.debugger.data.source.TetraDataSource
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

/**
 * @author okasurya on 2019-08-15.
 * Tetra Debugger is remote analytics debugger to automate data layer testing
 * by validating the data on testing server
 */
class TetraDebugger(private val context: Context) : CoroutineScope {

    val job = Job()
    val dataSource = TetraDataSource(context)
    val mapper = TetraMapper()


    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    /**
     * Initialize the test process, by validating the deviceId of the phone to testing server
     */
    fun init() {
        launchCatchError(coroutineContext, {
            val service = TetraService(context).makeRetrofitService()
            val request = TetraService.parse(mapper.parseInitRequest())
            val response = service.init(request).await()
            val parseResponse = mapper.parseInitResponse(response.body())
            dataSource.putStatus(parseResponse)
        }, {
            Timber.d(it)
        })
    }

    /**
     * send each data layer (currently only support gtm) to testing server
     */
    fun send(data: Map<String, Any>) {
        launchCatchError(coroutineContext, {
            if (dataSource.isWhitelisted()) {
                val service = TetraService(context).makeRetrofitService()
                val request = TetraService.parse(mapper.parseDebugRequest(dataSource.getUserId(), data))
                val response = service.init(request).await()
                mapper.parseDebugResponse(response.body())
            }
        }, {
            Timber.d(it)
        })
    }

    fun setUserId(value: String) {
        dataSource.putUserId(value)
    }

    fun cancel() {
        if (!job.isCancelled) {
            job.cancel()
        }
    }
}