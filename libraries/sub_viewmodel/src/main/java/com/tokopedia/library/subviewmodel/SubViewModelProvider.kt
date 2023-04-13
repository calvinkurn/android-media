package com.tokopedia.library.subviewmodel

import kotlinx.coroutines.CoroutineScope
import java.io.Closeable

/**
 * this interface is the attribute register which needs a sub-viewmodel to [SubViewModelScope]
 */
interface SubViewModelProvider : Closeable, SubViewModelScope {
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
