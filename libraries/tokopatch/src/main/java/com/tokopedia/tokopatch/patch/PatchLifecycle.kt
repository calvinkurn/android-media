package com.tokopedia.tokopatch.patch

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry

/**
 * Author errysuprayogi on 16,June,2020
 */
class PatchLifecycle: LifecycleOwner {

    private var registry: LifecycleRegistry = LifecycleRegistry(this)

    init {
        registry.handleLifecycleEvent(Lifecycle.Event.ON_START)
    }

    fun stopListening() {
        registry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
    }

    fun startListening() {
        registry.handleLifecycleEvent(Lifecycle.Event.ON_START)
    }

    override fun getLifecycle(): Lifecycle {
        return registry
    }
}