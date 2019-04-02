package com.tokopedia.abstraction.base.view.viewmodel

import android.arch.lifecycle.ViewModel
import kotlinx.coroutines.experimental.*
import kotlin.coroutines.experimental.CoroutineContext

abstract class BaseViewModel(private val baseDispatcher: CoroutineDispatcher): ViewModel(), CoroutineScope{
    protected val masterJob = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = baseDispatcher + masterJob

    open fun clear(){
        if (isActive && !masterJob.isCancelled){
            masterJob.children.map { it.cancel() }
        }
    }
}