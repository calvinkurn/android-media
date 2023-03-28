package com.tokopedia.product.detail.view.viewmodel.slicing

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * Created by yovi.putra on 24/03/23"
 * Project name: android-tokopedia-core
 **/
abstract class SliceViewModel {

    protected lateinit var scope: CoroutineScope

    private val coroutineContext by lazy { scope.coroutineContext }

    operator fun invoke(scope: CoroutineScope) {
        this.scope = scope
        afterInvoke()
    }

    open fun afterInvoke() {}

    protected fun launch(
        context: CoroutineContext = coroutineContext,
        block: suspend CoroutineScope.() -> Unit
    ) {
        scope.launch(context = context) {
            block()
        }
    }
}
