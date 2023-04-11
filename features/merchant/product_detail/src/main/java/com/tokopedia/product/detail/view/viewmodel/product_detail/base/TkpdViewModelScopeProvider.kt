package com.tokopedia.product.detail.view.viewmodel.product_detail.base

import kotlinx.coroutines.CoroutineScope

class TkpdViewModelScopeProvider : ViewModelScopeProvider {
    private var provider: (() -> CoroutineScope)? = null

    override fun register(provider: () -> CoroutineScope) {
        this.provider = provider
    }

    override val vmScope: CoroutineScope?
        get() = provider?.invoke()

    override fun close() {
        provider = null
    }
}
