package com.tokopedia.home.beranda.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.isActive
import kotlin.coroutines.CoroutineContext

abstract class BaseCoRoutineScope(private val baseDispatcher: CoroutineDispatcher) : CoroutineScope {
    protected val masterJob = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = baseDispatcher + masterJob

    open fun flush() {
        if (isActive && !masterJob.isCancelled) {
            masterJob.children.map { it.cancel() }
        }
    }

    open fun onCleared() {
        flush()
    }
}
