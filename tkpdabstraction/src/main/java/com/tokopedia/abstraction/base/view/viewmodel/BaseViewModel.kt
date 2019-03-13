package com.tokopedia.abstraction.base.view.viewmodel

import android.arch.lifecycle.ViewModel
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.isActive
import kotlin.coroutines.experimental.CoroutineContext

abstract class BaseViewModel(private val baseDispatcher: CoroutineDispatcher): ViewModel(), CoroutineScope {
    protected val masterJob = Job()

    override val coroutineContext: CoroutineContext
        get() = baseDispatcher + masterJob

    open fun clear(){
        if (isActive && !masterJob.isCancelled){
            masterJob.cancel()
        }
    }
}