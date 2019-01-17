package com.tokopedia.usecase.coroutines

import com.tokopedia.kotlin.extensions.coroutines.AppExecutors
import kotlinx.coroutines.experimental.*

abstract class UseCase<out T : Any> {

    protected var parentJob = SupervisorJob()
    private val localScope = CoroutineScope(AppExecutors.uiContext + parentJob)

    abstract suspend fun executeOnBackground(): T

    private suspend fun executeCatchError(): Result<T> = withContext(AppExecutors.bgContext){
        try {
            Success(executeOnBackground())
        } catch (throwable: Throwable) {
            Fail(throwable)
        }
    }

    fun execute(onSuccess: (T) -> Unit, onError: (Throwable) -> Unit) {
       cancelJobs()
        localScope.launch{
            try {
                val result = executeCatchError()
                when (result) {
                    is Success -> onSuccess(result.data)
                    is Fail -> onError(result.throwable)
                }
            } catch (throwable: Throwable) {
                if (throwable !is CancellationException)
                    onError(throwable)
            }
        }
    }

    fun cancelJobs() {
        localScope.coroutineContext.cancelChildren()
    }
}