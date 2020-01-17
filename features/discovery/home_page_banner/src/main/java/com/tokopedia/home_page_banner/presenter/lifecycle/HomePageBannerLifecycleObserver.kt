package com.tokopedia.home_page_banner.presenter.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.tokopedia.home_page_banner.presenter.handler.HomePageBannerActionHandler

object HomePageBannerLifecycleObserver : LifecycleObserver {

    private var actionHandler: HomePageBannerActionHandler? = null

    fun registerActionHandler(handler: HomePageBannerActionHandler) {
        this.actionHandler = handler
    }

    fun registerLifecycle(lifecycle: Lifecycle) {
        lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        actionHandler?.onStart()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        actionHandler?.onStop()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop(){
        actionHandler?.onStop()
    }
}