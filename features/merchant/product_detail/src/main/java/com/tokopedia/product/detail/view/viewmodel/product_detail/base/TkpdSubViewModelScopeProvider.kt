package com.tokopedia.product.detail.view.viewmodel.product_detail.base

import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class TkpdSubViewModelScopeProvider @Inject constructor(): SubViewModelScopeProvider {
    private var provider: (() -> CoroutineScope)? = null

    override fun registerProvider(provider: () -> CoroutineScope) {
        this.provider = provider
    }

    override val vmScope: CoroutineScope?
        get() = provider?.invoke()

    override fun close() {
        provider = null
    }
}
