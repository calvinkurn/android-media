package com.tokopedia.product.detail.view.viewmodel.product_detail.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModelV2(
    private val baseDispatcher: CoroutineDispatcher,
    subViewModelScopeProvider: SubViewModelScopeProvider,
) : ViewModel(), CoroutineScope {

    init {
        registerSubViewModelScopeProvider(subViewModelScopeProvider)
    }

    private fun registerSubViewModelScopeProvider(
        subViewModelScopeProvider: SubViewModelScopeProvider
    ) {
        subViewModelScopeProvider.registerProvider { viewModelScope }
    }

    override val coroutineContext: CoroutineContext
        get() = viewModelScope.coroutineContext + baseDispatcher

    /**
     * No need to call this on onDestroy activity/fragment
     * The job is automatically cleared
     */
    @Deprecated("Ne need to call this when onDestroy Activity/Fragment")
    open fun flush() {
        viewModelScope.coroutineContext[Job]?.cancelChildren()
    }
}
