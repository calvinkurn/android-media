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
    private var mViewModelScope: CoroutineScope? = null

    // store delegate to get SubViewModelMediator
    private var mMediator: SubViewModelMediator? = null

    override val viewModelScope: CoroutineScope
        get() = mViewModelScope
            ?: throw IllegalAccessException("viewModelScope is not registered yet, make sure your ViewModel extend ${ParentSubViewModel::class.simpleName}")

    override val mediator: SubViewModelMediator
        get() = mMediator
            ?: throw IllegalAccessException("Mediator is not registered yet, make sure your ViewModel extend ${ParentSubViewModel::class.simpleName}")

    /**
     * register [viewModelScope]
     * @param viewModelScope
     */
    override fun registerScope(viewModelScope: CoroutineScope) {
        validateMultipleUseProvider()
        this.mViewModelScope = viewModelScope
    }

    private fun validateMultipleUseProvider() {
        if (this.mViewModelScope != null) {
            val message = "This SubVieModelProvider is already in use by another ViewModel"
            val throwable = IllegalAccessException(message)
            Timber.e(throwable)
            throw throwable
        }
    }

    /**
     * register [SubViewModelMediator]
     * @param mediator
     */
    override fun registerMediator(mediator: SubViewModelMediator) {
        this.mMediator = mediator
    }

    /**
     * clear memory usage when this class is unused
     */
    override fun close() {
        mViewModelScope = null
        mMediator = null
    }
}
