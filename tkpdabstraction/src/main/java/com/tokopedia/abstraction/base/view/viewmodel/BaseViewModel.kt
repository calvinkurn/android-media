package com.tokopedia.abstraction.base.view.viewmodel

import android.arch.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel(private val baseDispatcher: CoroutineDispatcher): ViewModel(), CoroutineScope{
    protected val masterJob = Job()

    override val coroutineContext: CoroutineContext
        get() = baseDispatcher + masterJob

    open fun clear(){
        if (isActive && !masterJob.isCancelled){
            masterJob.cancel()
        }
    }
}