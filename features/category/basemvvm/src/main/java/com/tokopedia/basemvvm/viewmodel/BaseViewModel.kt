package com.tokopedia.basemvvm.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel(private val dispatcher : CoroutineDispatcher = Dispatchers.Main) : ViewModel(), CoroutineScope {

    private val viewModelJob = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = dispatcher + viewModelJob

    open fun doOnStart() {}

    open fun doOnCreate() {}

    open fun doOnPause() {}

    open fun doOnResume() {}

    open fun doOnStop() {}

    open fun doOnDestroy() {}

    open fun cancelJob(){
        if (isActive && !viewModelJob.isCancelled){
            viewModelJob.cancel()
        }
    }

    override fun onCleared() {
        super.onCleared()
        cancelJob()
    }

}
