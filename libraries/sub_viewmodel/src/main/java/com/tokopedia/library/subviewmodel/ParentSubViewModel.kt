package com.tokopedia.library.subviewmodel

import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.plus

/**
 * [ParentSubViewModel] is a class extend from [BaseViewModel] for easier implementation of [SubViewModel] support
 */
open class ParentSubViewModel(
    private val baseDispatcher: CoroutineDispatcher,
    private vararg val subViewModels: ISubViewModel
) : BaseViewModel(baseDispatcher), SubViewModelMediator {

    init {
        registerSubViewModelProvider(*subViewModels)
    }

    /**
     * register all properties in provider scope
     * @param subViewModels
     */
    private fun registerSubViewModelProvider(
        vararg subViewModels: ISubViewModel
    ) {
        subViewModels.forEach {
            it.registerScope { viewModelScope + baseDispatcher }
            it.registerMediator { this }
        }
    }

    /**
     * release memory usage in sub-view-model provider
     */
    override fun onCleared() {
        subViewModels.forEach { it.close() }
        super.onCleared()
    }
}
