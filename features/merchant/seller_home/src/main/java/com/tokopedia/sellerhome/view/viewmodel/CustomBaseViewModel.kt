package com.tokopedia.sellerhome.view.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * Created By @ilhamsuaib on 2020-02-27
 */

abstract class CustomBaseViewModel(baseDispatcher: CoroutineDispatcher) : BaseViewModel(baseDispatcher) {

    fun <T : Any> CoroutineScope.executeCall(
            liveData: MutableLiveData<Result<T>>,
            context: CoroutineContext = coroutineContext,
            onError: (Throwable) -> Unit = {},
            block: suspend () -> T
    ) {
        launch(context) {
            try {
                liveData.value = Success(withContext(Dispatchers.IO) {
                    block.invoke()
                })
            } catch (e: Exception) {
                liveData.value = Fail(e)
                onError(e)
            }
        }
    }
}