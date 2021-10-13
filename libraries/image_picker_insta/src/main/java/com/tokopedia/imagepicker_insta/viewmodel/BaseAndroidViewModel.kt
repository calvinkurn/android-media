package com.tokopedia.imagepicker_insta.viewmodel

import android.app.Application
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.isActive
import kotlin.coroutines.CoroutineContext

abstract class BaseAndroidViewModel(app:Application): AndroidViewModel(app), CoroutineScope {
    @VisibleForTesting
    val masterJob = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = masterJob

    fun flush(){
        if (isActive && !masterJob.isCancelled){
            masterJob.children.map { it.cancel() }
        }
    }
}