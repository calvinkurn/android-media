package com.tokopedia.abstraction.base.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel(private val baseDispatcher: CoroutineDispatcher): ViewModel(), CoroutineScope{

    override val coroutineContext: CoroutineContext
        get() = viewModelScope.coroutineContext + baseDispatcher

    /**
     * No need to call this on onDestroy activity/fragment
     * The job is automatically cleared
     */
    @Deprecated("Ne need to call this when onDestroy Activity/Fragment")
    open fun flush(){
        viewModelScope.coroutineContext[Job]?.cancelChildren()
    }
}