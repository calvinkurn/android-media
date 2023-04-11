package com.tokopedia.product.detail.view.viewmodel.product_detail.base

import kotlinx.coroutines.CoroutineScope
import java.io.Closeable

interface SubViewModelProvider : Closeable, SubViewModelScope {
    fun registerScope(viewModelScope: () -> CoroutineScope)

    fun registerMediator(mediator: () -> SubViewModelMediator)
}
