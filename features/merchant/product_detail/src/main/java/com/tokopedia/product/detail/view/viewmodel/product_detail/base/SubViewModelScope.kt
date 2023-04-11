package com.tokopedia.product.detail.view.viewmodel.product_detail.base

import kotlinx.coroutines.CoroutineScope

interface SubViewModelScope {
    val vmScope: CoroutineScope?
}
