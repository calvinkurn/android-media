package com.tokopedia.search.result.product.safesearch

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner

class SafeSearchAppLifecycleObserver(
    private val safeSearchPreference: MutableSafeSearchPreference,
) : LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppGoBackground() {
        if (safeSearchPreference.isShowAdult) {
            safeSearchPreference.isShowAdult = false
            ProcessLifecycleOwner.get().lifecycle.removeObserver(this)
        }
    }
}
