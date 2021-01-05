package com.tokopedia.tokopatch

import android.app.Application
import androidx.annotation.Keep
import androidx.core.app.PatchService
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner


/**
 * Author errysuprayogi on 03,February,2020
 */
class TokoPatch (val app: Application) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onForegroud() {
        PatchService.startService(app)
    }

    @Keep
    companion object {
        private val TAG = TokoPatch::class.java.simpleName
        @JvmStatic
        fun init(application: Application) {
            TokoPatch(application)
        }
    }

    init {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }
}
