package com.tokopedia.product.detail.view.viewmodel.product_detail.base

import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher

/**
 * [BaseViewModelV2] is a class for easier implementation of [SubViewModel] support
 */
open class BaseViewModelV2(
    baseDispatcher: CoroutineDispatcher,
    private val subViewModelProvider: SubViewModelProvider
) : BaseViewModel(baseDispatcher), SubViewModelMediator {

    init {
        registerSubViewModelProvider(subViewModelProvider)
    }

    /**
     * register all properties in provider scope
     * @param subViewModelProvider
     */
    private fun registerSubViewModelProvider(
        subViewModelProvider: SubViewModelProvider
    ) {
        subViewModelProvider.registerScope { viewModelScope }
        subViewModelProvider.registerMediator { this }
    }

    /**
     * release memory usage in sub-view-model provider
     */
    override fun onCleared() {
        subViewModelProvider.close()
        super.onCleared()
    }
}
