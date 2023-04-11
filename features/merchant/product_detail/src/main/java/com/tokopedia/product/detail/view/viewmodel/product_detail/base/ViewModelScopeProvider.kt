package com.tokopedia.product.detail.view.viewmodel.product_detail.base

import kotlinx.coroutines.CoroutineScope
import java.io.Closeable

interface ViewModelScopeProvider : Closeable, ViewModelSlice {
    fun register(provider: () -> CoroutineScope)
}
