package com.tokopedia.usecase.coroutines

import com.tokopedia.kotlin.extensions.coroutines.AppExecutors
import com.tokopedia.kotlin.extensions.coroutines.thenOnUI
import kotlinx.coroutines.experimental.*

abstract class UseCase<out T: Any> {

    protected var parentJob: Job = Job()

    abstract suspend fun executeOnBackground(): T

    /*private suspend fun executeCatchError(): Result<T>{
        return try {
            Success(executeOnBackground())
        } catch (throwable: Throwable){
            RequestError(throwable)
        }
    }*/

    fun execute(onSuccess: (T) -> Unit, onError: (Throwable) -> Unit){
        parentJob.cancel()
        parentJob = Job()
        GlobalScope.launch(AppExecutors.uiContext + parentJob) {
            try {
                async(AppExecutors.bgContext) { executeOnBackground() }
                        .thenOnUI(this){ onSuccess.invoke(it) }
            } catch (throwable: Throwable){
                onError(throwable)
            }

        }
    }

    fun unsubscribe(){
        parentJob.cancel()
    }
}