package com.tokopedia.search.result.product.safesearch

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class SafeSearchActivityLifecycleObserver(
    private val safeSearchPreference: SafeSearchPreference,
) : LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onActivityDestroyed() {
        if(safeSearchPreference.isShowAdult) {
            safeSearchPreference.isShowAdult = false
            SafeSearchLifecycleHelper.unregisterLifecycleObserver()
        }
    }
}
