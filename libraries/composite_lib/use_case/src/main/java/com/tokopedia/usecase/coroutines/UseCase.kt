package com.tokopedia.usecase.coroutines

import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.*

abstract class UseCase<out T : Any>(
        private val defaultDispatchers: CoroutineDispatcher = Dispatchers.Default,
        mainDispatchers: CoroutineDispatcher = Dispatchers.Main
) {

    protected var parentJob = SupervisorJob()
    private val localScope = CoroutineScope(mainDispatchers + parentJob)
    protected var requestParams = RequestParams.EMPTY

    abstract suspend fun executeOnBackground(): T

    private suspend fun executeCatchError(): Result<T> = withContext(defaultDispatchers) {
        try {
            Success(executeOnBackground())
        } catch (throwable: Throwable) {
            Fail(throwable)
        }
    }

    fun execute(onSuccess: (T) -> Unit, onError: (Throwable) -> Unit, requestParams: RequestParams = RequestParams.EMPTY) {
        cancelJobs()
        localScope.launchCatchError(block = {
            this.requestParams = requestParams
            val result = executeCatchError()
            when (result) {
                is Success -> onSuccess(result.data)
                is Fail -> onError(result.throwable)
            }
        }) {
            if (it !is CancellationException)
                onError(it)
        }
    }

    fun cancelJobs() {
        localScope.coroutineContext.cancelChildren()
    }
}