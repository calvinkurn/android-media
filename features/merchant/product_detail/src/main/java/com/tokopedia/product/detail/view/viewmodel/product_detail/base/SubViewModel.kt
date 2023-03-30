package com.tokopedia.product.detail.view.viewmodel.product_detail.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * Created by yovi.putra on 24/03/23"
 * Project name: android-tokopedia-core
 **/

/***
 *
 */
@Suppress("LateinitUsage")
abstract class SubViewModel {

    private lateinit var mCoroutineScope: CoroutineScope

    private var mMediator: SubViewModelMediator? = null

    fun register(scope: CoroutineScope, mediator: SubViewModelMediator? = null) {
        if (::mCoroutineScope.isInitialized) {
            throw IllegalArgumentException("${this::class.java.simpleName} can only be registered once")
        }

        this.mCoroutineScope = scope
        this.mMediator = mediator
    }

    protected fun launch(
        context: CoroutineContext = mCoroutineScope.coroutineContext,
        block: suspend CoroutineScope.() -> Unit
    ) {
        mCoroutineScope.launch(context = context) {
            block()
        }
    }

    @Suppress("UNCHECKED_CAST")
    protected fun <T : SubViewModelMediator> getMediator(): T {
        if (mMediator == null) {
            throw IllegalArgumentException("Mediator cannot be null")
        }

        return mMediator as T
    }
}
