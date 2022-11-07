package com.tokopedia.search.result.product.safesearch

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class SafeSearchActivityLifecycleObserver(
    private val safeSearchPreference: MutableSafeSearchPreference,
) : LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onActivityDestroyed() {
        safeSearchPreference.isShowAdult = false
    }
}
