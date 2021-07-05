package com.tokopedia.sellerhome.view.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

/**
 * Created By @ilhamsuaib on 2020-02-27
 */

abstract class CustomBaseViewModel(private val dispatchers: CoroutineDispatchers) : BaseViewModel(dispatchers.main) {

    protected fun <T : Any> CoroutineScope.executeCall(
            liveData: MutableLiveData<Result<T>>,
            context: CoroutineContext = coroutineContext,
            onError: suspend (t: Throwable) -> Unit = {},
            block: suspend () -> T
    ) {
        launchCatchError(
                context = context,
                block = {
                    liveData.value = Success(withContext(dispatchers.io) {
                        block()
                    })
                },
                onError = {
                    liveData.value = Fail(it)
                    onError(it)
                }
        )
    }
}