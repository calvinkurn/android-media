package com.tokopedia.product.detail.view.viewmodel.product_detail.base

import kotlinx.coroutines.CoroutineScope
import java.io.Closeable

interface SubViewModelScopeProvider : Closeable, SubViewModelScope {
    fun registerProvider(provider: () -> CoroutineScope)
}
