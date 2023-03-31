package com.tokopedia.product.detail.view.viewmodel.product_detail.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * Created by yovi.putra on 24/03/23"
 * Project name: android-tokopedia-core
 **/

/***
 * [SubViewModel] is separating the UI logic in the main viewmodel into a smaller viewmodel by delegating the sub-viewmodel.
 * Separate event and state according to their respective contexts.
 */
@Suppress("LateinitUsage")
abstract class SubViewModel {

    // scope of main-viewmodel which is passed at register.
    // so that when processing a task in the sub-viewmodel,
    // the scope used is the scope of the main-viewmodel.
    private lateinit var mCoroutineScope: CoroutineScope

    // an interface to communicate with outer class
    private var mMediator: SubViewModelMediator? = null

    /***
     * a sub-viewmodel must be registered before use
     * @param scope
     * @param mediator
     */
    fun register(scope: CoroutineScope, mediator: SubViewModelMediator? = null) {
        if (::mCoroutineScope.isInitialized) {
            throw IllegalArgumentException("${this::class.java.simpleName} can only be registered once")
        }

        this.mCoroutineScope = scope
        this.mMediator = mediator
    }

    /***
     * helper function to process something in a coroutine
     * @param context by default [mCoroutineScope.coroutineContext]
     * @param block
     */
    protected fun launch(
        context: CoroutineContext = mCoroutineScope.coroutineContext,
        block: suspend CoroutineScope.() -> Unit
    ) {
        mCoroutineScope.launch(context = context) {
            block()
        }
    }

    /***
     * helper function to get mediator base on type
     * @return inheritance subview model mediator
     */
    @Suppress("UNCHECKED_CAST")
    protected fun <T : SubViewModelMediator> getMediator(): T {
        if (mMediator == null) {
            throw IllegalArgumentException("Mediator cannot be null")
        }

        return mMediator as T
    }
}
