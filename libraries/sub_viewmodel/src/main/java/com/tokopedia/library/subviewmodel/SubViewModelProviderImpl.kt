package com.tokopedia.library.subviewmodel

import kotlinx.coroutines.CoroutineScope
import timber.log.Timber
import javax.inject.Inject

/**
 * [SubViewModelProvider] implementation
 */
class SubViewModelProviderImpl @Inject constructor() : SubViewModelProvider {
    // store delegate to get viewModelScopeProvider
    private var viewModelScopeProvider: (() -> CoroutineScope)? = null

    // store delegate to get SubViewModelMediator
    private var mediatorProvider: (() -> SubViewModelMediator)? = null

    override val viewModelScope: CoroutineScope
        get() = viewModelScopeProvider?.invoke()
            ?: throw IllegalAccessException("viewModelScope is not registered yet, make sure your ViewModel extend ${BaseViewModelV2::class.simpleName}")

    override val mediator: SubViewModelMediator?
        get() = mediatorProvider?.invoke()

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
