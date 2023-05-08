package com.tokopedia.library.subviewmodel

import kotlinx.coroutines.CoroutineScope
import timber.log.Timber

/**
 * Created by yovi.putra on 24/03/23"
 * Project name: android-tokopedia-core
 **/

/***
 * [SubViewModel] is separating the UI logic in the main viewmodel into a smaller viewmodel by delegating the sub-viewmodel.
 * Separate event and state according to their respective contexts.
 */
abstract class SubViewModel : ISubViewModel {
    // store delegate to get viewModelScopeProvider
    private var viewModelScopeProvider: (() -> CoroutineScope)? = null

    // store delegate to get SubViewModelMediator
    private var mediatorProvider: (() -> SubViewModelMediator)? = null

    override val viewModelScope: CoroutineScope
        get() = viewModelScopeProvider?.invoke()
            ?: throw IllegalAccessException("viewModelScope is not registered yet, make sure your ViewModel extend ${ParentSubViewModel::class.simpleName}")

    override val mediator: SubViewModelMediator
        get() = mediatorProvider?.invoke()
            ?: throw IllegalAccessException("Mediator is not registered yet, make sure your ViewModel extend ${ParentSubViewModel::class.simpleName}")

    /**
     * register [viewModelScope] and store to [viewModelScopeProvider]
     * @param viewModelScope delegate
     */
    override fun registerScope(viewModelScope: () -> CoroutineScope) {
        validateMultipleUseProvider()
        this.viewModelScopeProvider = viewModelScope
    }

    private fun validateMultipleUseProvider() {
        if (this.viewModelScopeProvider != null) {
            val message = "This SubVieModelProvider is already in use by another ViewModel"
            val throwable = IllegalAccessException(message)
            Timber.e(throwable)
            throw throwable
        }
    }

    /**
     * register [SubViewModelMediator] and store to [mediatorProvider]
     * @param mediator delegate
     */
    override fun registerMediator(mediator: () -> SubViewModelMediator) {
        this.mediatorProvider = mediator
    }

    /**
     * clear memory usage when this class has closed
     */
    override fun close() {
        viewModelScopeProvider = null
        mediatorProvider = null
    }
}
