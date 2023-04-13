package com.tokopedia.library.subviewmodel

import kotlinx.coroutines.CoroutineScope

/**
 * this interface is the attribute which needs a sub-viewmodel
 */
interface SubViewModelScope {
    val viewModelScope: CoroutineScope

    val mediator: SubViewModelMediator?
}
