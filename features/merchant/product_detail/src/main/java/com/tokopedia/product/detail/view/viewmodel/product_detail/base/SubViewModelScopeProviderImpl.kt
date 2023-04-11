package com.tokopedia.product.detail.view.viewmodel.product_detail.base

import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class SubViewModelScopeProviderImpl @Inject constructor(): SubViewModelScopeProvider {
    private var provider: (() -> CoroutineScope)? = null

    override fun register(provider: () -> CoroutineScope) {
        this.provider = provider
    }

    override val viewModelScope: CoroutineScope?
        get() = provider?.invoke()

    override fun close() {
        provider = null
    }
}
