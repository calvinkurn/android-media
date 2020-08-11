package com.tokopedia.play.view.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*

/**
 * Created by jegul on 24/04/20
 */
open class PlayBaseViewModel(dispatcher: CoroutineDispatcher) : ViewModel() {
    protected val masterJob = SupervisorJob()

    protected val scope = CoroutineScope(masterJob + dispatcher)

    protected open fun flush() {
        if (scope.isActive && !masterJob.isCancelled){
            masterJob.cancelChildren()
        }
    }

    override fun onCleared() {
        super.onCleared()
        flush()
    }
}