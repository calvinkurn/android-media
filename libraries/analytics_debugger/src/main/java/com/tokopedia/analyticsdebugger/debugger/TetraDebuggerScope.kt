package com.tokopedia.analyticsdebugger.debugger

import android.content.Context
import com.tokopedia.analyticsdebugger.debugger.TetraDebugger
import com.tokopedia.analyticsdebugger.debugger.data.mapper.TetraMapper
import com.tokopedia.analyticsdebugger.debugger.data.network.TetraService
import com.tokopedia.analyticsdebugger.debugger.data.source.TetraDataSource
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

class TetraDebuggerScope(private val context: Context) : CoroutineScope, TetraDebugger {

    val job = Job()
    val dataSource = TetraDataSource(context)
    val mapper = TetraMapper()


    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    override fun init() {
        launchCatchError(coroutineContext, {
            val service = TetraService(context).makeRetrofitService()
            val request = TetraService.parse(mapper.parseInitRequest())
            val response = service.init(request)
            val parseResponse = mapper.parseInitResponse(response.body())
            dataSource.putStatus(parseResponse)
        }, {
            Timber.d(it)
        })
    }

    override fun send(data: Map<String, Any>) {
        launchCatchError(coroutineContext, {
            if (dataSource.isWhitelisted()) {
                val service = TetraService(context).makeRetrofitService()
                val request = TetraService.parse(mapper.parseDebugRequest(dataSource.getUserId(), data))
                val response = service.send(request)
                mapper.parseDebugResponse(response.body())
            }
        }, {
            Timber.d(it)
        })
    }

    override fun setUserId(value: String) {
        dataSource.putUserId(value)
    }

    override fun cancel() {
        if (!job.isCancelled) {
            job.cancel()
        }
    }

    companion object {
        @JvmStatic
        fun getInstance(context: Context): TetraDebugger {
            return TetraDebuggerScope(context)
        }
    }
}