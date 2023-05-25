package com.tokopedia.library.subviewmodel

import kotlinx.coroutines.CoroutineScope
import java.io.Closeable

/**
 * This interface is the attribute register required by the [SubViewModel]
 */
interface ISubViewModel : Closeable {

    // scope of ParentViewModel which is passed at register.
    // for processing a task in the SubViewModel,
    val viewModelScope: CoroutineScope

    // an interface to access data to ParentViewModel
    val mediator: SubViewModelMediator

    /**
     * register [viewModelScope]
     * @param viewModelScope
     */
    fun registerScope(viewModelScope: CoroutineScope)

    /**
     * register [SubViewModelMediator]
     * @param mediator
     */
    fun registerMediator(mediator: SubViewModelMediator)
}
