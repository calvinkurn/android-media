package com.tokopedia.gamification.giftbox.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.isActive
import kotlin.coroutines.CoroutineContext

abstract class BaseAndroidViewModel(private val baseDispatcher: CoroutineDispatcher, app:Application): AndroidViewModel(app), CoroutineScope {
    protected val masterJob = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = baseDispatcher + masterJob

    open fun flush(){
        if (isActive && !masterJob.isCancelled){
            masterJob.children.map { it.cancel() }
        }
    }
}