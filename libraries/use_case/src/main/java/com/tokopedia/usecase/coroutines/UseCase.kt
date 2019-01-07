package com.tokopedia.usecase.coroutines

import kotlinx.coroutines.experimental.*

abstract class UseCase<out T : Any> {

    protected var parentJob = SupervisorJob()
    private val localScope = CoroutineScope(Dispatchers.Main + parentJob)

    abstract suspend fun executeOnBackground(): T

    private suspend fun executeCatchError(): Result<T> = withContext(Dispatchers.Default){
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