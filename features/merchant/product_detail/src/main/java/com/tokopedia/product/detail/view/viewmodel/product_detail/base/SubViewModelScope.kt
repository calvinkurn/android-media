package com.tokopedia.product.detail.view.viewmodel.product_detail.base

import kotlinx.coroutines.CoroutineScope

/**
 * this interface is the attribute which needs a sub-viewmodel
 */
interface SubViewModelScope {
    val viewModelScope: CoroutineScope?

    val mediator: SubViewModelMediator?
}
