package com.tokopedia.thankyou_native.presentation.views

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

object TimerViewLifeCycleObserver : LifecycleObserver {
    private var actionHandler: ViewActionHandler? = null

    fun registerActionHandler(handler: ViewActionHandler) {
        this.actionHandler = handler
    }

    fun registerLifecycle(lifecycle: Lifecycle) {
        lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start() {
        this.actionHandler?.onStart()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        this.actionHandler?.onStop()
    }
}

interface ViewActionHandler {
    fun onStart()
    fun onStop()
}