package com.tokopedia.library.subviewmodel

import kotlinx.coroutines.CoroutineScope
import java.io.Closeable

/**
 * this interface is the attribute register which needs a sub-viewmodel to [SubViewModelScope]
 */
interface ISubViewModel : Closeable {

    // scope of ParentViewModel which is passed at register.
    // for processing a task in the SubViewModel,
    val viewModelScope: CoroutineScope

    // an interface to access data to ParentViewModel
    val mediator: SubViewModelMediator

    /**
     * register [viewModelScope]
     * @param viewModelScope delegate
     */
    fun registerScope(viewModelScope: () -> CoroutineScope)

    /**
     * register [SubViewModelMediator]
     * @param mediator delegate
     */
    fun registerMediator(mediator: () -> SubViewModelMediator)
}
