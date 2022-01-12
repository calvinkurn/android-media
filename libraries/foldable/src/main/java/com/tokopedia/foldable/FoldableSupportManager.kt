package com.tokopedia.foldable

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.window.WindowInfoRepo
import androidx.window.WindowLayoutInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FoldableSupportManager(val windowInfoRepo: WindowInfoRepo, val callback: Callback) : LifecycleObserver {

    private var layoutUpdatesJob: Job? = null

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    open fun onCreate() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    open fun onStart() {
        layoutUpdatesJob = CoroutineScope(Dispatchers.Main).launch {
            windowInfoRepo.windowLayoutInfo()
                .collect { newLayoutInfo ->
                    callback.onChangeLayout(FoldableInfo(newLayoutInfo))
                }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    open fun onStop() {
        layoutUpdatesJob?.cancel()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    open fun onResume() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    open fun onPause() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    open fun onDestroy() {

    }

    interface Callback {
        fun onChangeLayout(foldableInfo: FoldableInfo)
    }
}