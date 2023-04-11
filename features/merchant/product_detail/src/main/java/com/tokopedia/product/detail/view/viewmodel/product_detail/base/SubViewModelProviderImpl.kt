package com.tokopedia.product.detail.view.viewmodel.product_detail.base

import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class SubViewModelProviderImpl @Inject constructor() : SubViewModelProvider {
    private var viewModelScopeProvider: (() -> CoroutineScope)? = null

    private var mediatorProvider: (() -> SubViewModelMediator)? = null

    override fun registerScope(viewModelScope: () -> CoroutineScope) {
        this.viewModelScopeProvider = viewModelScope
    }
    override fun registerMediator(mediator: () -> SubViewModelMediator) {
        this.mediatorProvider = mediator
    }

    override val viewModelScope: CoroutineScope?
        get() = viewModelScopeProvider?.invoke()

    override val mediator: SubViewModelMediator?
        get() = mediatorProvider?.invoke()

    override fun close() {
        viewModelScopeProvider = null
        mediatorProvider = null
    }
}
