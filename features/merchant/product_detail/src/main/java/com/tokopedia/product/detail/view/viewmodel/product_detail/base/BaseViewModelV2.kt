package com.tokopedia.product.detail.view.viewmodel.product_detail.base

import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher

open class BaseViewModelV2(
    baseDispatcher: CoroutineDispatcher,
    subViewModelScopeProvider: SubViewModelScopeProvider,
) : BaseViewModel(baseDispatcher) {

    init {
        registerSubViewModelScopeProvider(subViewModelScopeProvider)
    }

    private fun registerSubViewModelScopeProvider(
        subViewModelScopeProvider: SubViewModelScopeProvider
    ) {
        subViewModelScopeProvider.register { viewModelScope }
    }
}
