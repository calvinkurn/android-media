package com.tokopedia.sellerhome.view.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.coroutines.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * Created By @ilhamsuaib on 2020-02-27
 */

abstract class CustomBaseViewModel(dispatchers: CoroutineDispatchers) : BaseViewModel(dispatchers.io) {

    protected fun <T : Any> CoroutineScope.executeCall(
            liveData: MutableLiveData<Result<T>>,
            context: CoroutineContext = coroutineContext,
            onError: suspend (t: Throwable) -> Unit = {},
            block: suspend () -> T
    ) {
        launchCatchError(
                context = context,
                block = {
                    liveData.postValue(Success(block()))
                },
                onError = {
                    liveData.postValue(Fail(it))
                    onError(it)
                }
        )
    }
}