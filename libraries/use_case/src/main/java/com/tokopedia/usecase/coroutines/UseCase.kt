package com.tokopedia.usecase.coroutines

import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.experimental.*

abstract class UseCase<out T : Any> {

    protected var parentJob = SupervisorJob()
    private val localScope = CoroutineScope(Dispatchers.Main + parentJob)

    abstract suspend fun executeOnBackground(): T

    private suspend fun executeCatchError(): Result<T> = withContext(Dispatchers.Default) {
        try {
            Success(executeOnBackground())
        } catch (throwable: Throwable) {
            Fail(throwable)
        }
    }

    fun execute(onSuccess: (T) -> Unit, onError: (Throwable) -> Unit) {
        cancelJobs()
        localScope.launchCatchError(block = {
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