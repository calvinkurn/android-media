package com.tokopedia.abstraction.base.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel(private val baseDispatcher: CoroutineDispatcher): ViewModel(), CoroutineScope{
    @Deprecated("no longer used")
    protected val masterJob = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = viewModelScope.coroutineContext

    @Deprecated("no longer used")
    open fun flush(){
        //no-op. already handled in viewModelScope
    }
}