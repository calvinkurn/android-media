package com.tokopedia.usecase.coroutines

import com.tokopedia.kotlin.extensions.coroutines.AppExecutors
import kotlinx.coroutines.experimental.*

abstract class UseCase<out T : Any> {

    protected var parentJob: Job = Job()

    abstract suspend fun executeOnBackground(): T

    private suspend fun executeCatchError(): Result<T> {
        return try {
            Success(executeOnBackground())
        } catch (throwable: Throwable) {
            Fail(throwable)
        }
    }

    fun createJob(): Deferred<Result<T>> {
        return GlobalScope.async(AppExecutors.bgContext) {
            executeCatchError()
        }
    }

    fun execute(onSuccess: (T) -> Unit, onError: (Throwable) -> Unit) {
        parentJob.cancel()
        parentJob = Job()
        GlobalScope.launch(AppExecutors.uiContext + parentJob) {
            try {
                val result = async(AppExecutors.bgContext) {
                    executeCatchError()
                }.await()
                when (result) {
                    is Success -> onSuccess(result.data)
                    is Fail -> onError(result.throwable)
                }
            } catch (throwable: Throwable) {
                onError(throwable)
            }
        }
    }

    fun unsubscribe() {
        parentJob.cancel()
    }
}