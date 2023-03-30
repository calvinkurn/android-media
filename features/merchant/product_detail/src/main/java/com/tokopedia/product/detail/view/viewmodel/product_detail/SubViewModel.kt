package com.tokopedia.product.detail.view.viewmodel.product_detail

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * Created by yovi.putra on 24/03/23"
 * Project name: android-tokopedia-core
 **/

@Suppress("LateinitUsage")
abstract class SubViewModel {

    protected lateinit var coroutineScope: CoroutineScope

    private val mCoroutineContext by lazy { coroutineScope.coroutineContext }

    fun register(scope: CoroutineScope) {
        if (::coroutineScope.isInitialized) {
            throw IllegalArgumentException("${this::class.java.simpleName} can only be registered once")
        }
        this.coroutineScope = scope
    }

    protected fun launch(
        context: CoroutineContext = mCoroutineContext,
        block: suspend CoroutineScope.() -> Unit
    ) {
        coroutineScope.launch(context = context) {
            block()
        }
    }
}
