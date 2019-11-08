package com.tokopedia.promotionstarget.presentation.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.isActive
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel(private val baseDispatcher: CoroutineDispatcher): ViewModel(), CoroutineScope {
    protected val masterJob = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = baseDispatcher + masterJob

    open fun clear(){
        if (isActive && !masterJob.isCancelled){
            masterJob.children.map { it.cancel() }
        }
    }
}