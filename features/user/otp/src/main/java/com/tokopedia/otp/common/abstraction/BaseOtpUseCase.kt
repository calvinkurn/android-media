package com.tokopedia.otp.common.abstraction

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

/**
 * Created by Ade Fulki on 03/06/20.
 */

abstract class BaseOtpUseCase<out T>(dispatcherProvider: CoroutineDispatchers) {

    protected var job: Job = SupervisorJob()

    protected var coroutineContext: CoroutineContext = dispatcherProvider.io + job

    abstract suspend fun getData(parameter: Map<String, Any>): T

    fun cancelJobs() {
        job.children.map {
            it.cancel()
        }
    }
}