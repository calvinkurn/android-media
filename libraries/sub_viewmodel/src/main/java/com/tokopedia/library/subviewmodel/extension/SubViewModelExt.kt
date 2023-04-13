package com.tokopedia.library.subviewmodel.extension

import com.tokopedia.library.subviewmodel.BaseViewModelV2
import com.tokopedia.library.subviewmodel.SubViewModel
import com.tokopedia.library.subviewmodel.SubViewModelMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/***
 * helper function to process something in a coroutine
 * @param context by default [viewModelScope.coroutineContext]
 * @param block
 */
fun SubViewModel.launch(
    context: CoroutineContext? = viewModelScope.coroutineContext,
    block: suspend CoroutineScope.() -> Unit
) {
    viewModelScope.launch(context = context ?: viewModelScope.coroutineContext) {
        block()
    }
}

/***
 * helper function to get mediator base on type
 * @return inheritance subview model mediator
 */
@Suppress("UNCHECKED_CAST")
fun <T : SubViewModelMediator> SubViewModel.getMediator(): Lazy<T> = lazy {
    if (mediator == null) {
        throw IllegalAccessException(
            "Mediator is not registered yet, make sure your ViewModel extend ${BaseViewModelV2::class.simpleName}"
        )
    }

    mediator as T
}
