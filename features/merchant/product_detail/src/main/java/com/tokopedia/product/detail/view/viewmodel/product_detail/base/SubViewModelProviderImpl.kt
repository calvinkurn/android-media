package com.tokopedia.product.detail.view.viewmodel.product_detail.base

import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

/**
 * [SubViewModelProvider] implementation
 */
class SubViewModelProviderImpl @Inject constructor() : SubViewModelProvider {
    // store delegate to get viewModelScopeProvider
    private var viewModelScopeProvider: (() -> CoroutineScope)? = null

    // store delegate to get SubViewModelMediator
    private var mediatorProvider: (() -> SubViewModelMediator)? = null

    override val viewModelScope: CoroutineScope?
        get() = viewModelScopeProvider?.invoke()

    override val mediator: SubViewModelMediator?
        get() = mediatorProvider?.invoke()

    /**
     * register [viewModelScope] and store to [viewModelScopeProvider]
     * @param viewModelScope delegate
     */
    override fun registerScope(viewModelScope: () -> CoroutineScope) {
        this.viewModelScopeProvider = viewModelScope
    }

    /**
     * register [SubViewModelMediator] and store to [mediatorProvider]
     * @param mediator delegate
     */
    override fun registerMediator(mediator: () -> SubViewModelMediator) {
        this.mediatorProvider = mediator
    }

    /**
     * clear memory usage when this class has closed
     */
    override fun close() {
        viewModelScopeProvider = null
        mediatorProvider = null
    }
}
