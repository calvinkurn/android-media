package com.tokopedia.search.result.product.safesearch

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class SafeSearchAppLifecycleObserver(
    private val safeSearchPreference: SafeSearchPreference,
) : LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onAppGoBackground() {
        if(safeSearchPreference.isShowAdult) {
            safeSearchPreference.isShowAdult = false
            SafeSearchLifecycleHelper.unregisterLifecycleObserver()
        }
    }
}
